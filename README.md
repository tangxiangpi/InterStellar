# InterStellar
![InterStellar_license](https://img.shields.io/badge/license-BSD--3--Clause-brightgreen.svg)
![InterStellar_core_tag](https://img.shields.io/badge/InterStellar%20core-1.0.0-brightgreen.svg)
![InterStellar_plugin_tag](https://img.shields.io/badge/InterStellar%20plugin-1.0.0-brightgreen.svg)

InterStellar是一个基于接口的组件间通信方案，包括同进程的本地接口调用和跨进程接口调用。
在InterStellar的世界里，不需要任何aidl接口及Service，IPC通信就和本地通信一样简单、方便。

**注:之所以分成本地服务和远程服务这两种，是由于本地服务的接口可以传递各种类型的参数和返回值，而远程接口则受binder通信数据类型的限制，参数和返回值只能是基本类型或者实现了Parcelable接口的自定义类型。**

# 特色

+ 无需开发者进行bindService()操作,也不用定义Service,不需要定义任何aidl接口即可实现IPC通信

+ 同步获取远程服务。抛弃了bindService()这种异步获取的方式，改造成了同步获取

+ 生命周期自动管理。可根据Fragment或Activity的生命周期进行提高或降低服务提供方进程的操作

+ 支持IPC的Callback，并且支持跨进程的事件总线

+ 采用"接口+数据结构"的方式来实现组件间通信，这种方式相比协议的方式在于实现简单，维护方便

**注意这里的服务不是Android中四大组件的Service,而是指提供的接口与实现。为了表示区分，后面的服务均是这个含义，而Service则是指Android中的组件。**

InterSteallar和其他组件间通信方案的对比如下:

|       |    使用方便性     | 代码侵入性  |   互操作性    |  是否支持IPC   |  是否支持跨进程事件总线  |  是否支持页面跳转  |
| :---: | :-------: | :----------: |:----------: |:----------: |:----------: |:----------: |
| InterSteallar |  好     |   较小     |    好    |    Yes    |   Yes    |   No     |
| DDComponentForAndroid |  较差      |   较大     |    差    |   No     |   No    |   Yes     |
| ARouter |  较好      |   较大     |    差    |   No     |   No    |    Yes    |


# 接入方式
首先在buildscript中添加classpath(以0.9.5为例):
```groovy
    classpath "org.qiyi.video.mcg.arch:plugin:0.9.5"
```
这两个分别是核心代码库和gradle插件库的路径。
在Application或library Module中使用核心库:
```groovy
    implementation 'org.qiyi.video.mcg.arch:core:0.9.5'
```
在application Module中使用gradle插件:
```groovy
    apply plugin: 'org.qiyi.video.mcg.arch.plugin'
```

# 使用方式
## 为Dispatcher配置进程
由于Dispatcher负责管理所有进程信息，所以它应该运行在存活时间最长的进程中。
如果不进行配置，Dispatcher默认运行在主进程中。
但是考虑到在有些App中，主进程不一定是存活时间最长的(比如音乐播放App中往往是播放进程的存活时间最长),
所以出现这种情况时开发者应该在application module的build.gradle中为Dispatcher配置进程名，如下:
```groovy
    dispatcher{
        process ":downloader"
    }
```
在这里，":downloader"进程是存活时间最长的.

## 初始化
最好是在自己进程的Application中进行初始化(每个进程都有自己的InterStellar对象)，代码如下:
```java
    InterStellar.init(Context);
```
## 本地服务的注册与使用
### 本地服务注册
本地服务的注册有两种方法，一种是直接调用接口的全路径名和接口的实现，如下:
```java
    InterStellar.registerLocalService(ICheckApple.class.getCanonicalName(),new CheckApple());
```
还有一种是调用接口class和接口的实现，其实在内部也是获取了它的全路径名,如下:
```java
    InterStellar.registerLocalService(ICheckApple.class,new CheckApple());
```
其中ICheckApple.class为接口，虽然也可以采用下面这种方式注册:
```java
    InterStellar.registerLocalService("wang.imallen.blog.moduleexportlib.apple.ICheckApple",CheckAppleImpl.getInstance());
```
**但是考虑到混淆问题，非常不推荐使用这种方式进行注册**，除非双方能够协商一致使用这个key(因为实际上InterStellar只需要保证有一个唯一的key与该服务对应即可).

### 本地服务使用
注册完之后，与服务提供方同进程的任何模块都可以调用该服务,获取服务的方式与注册对应，也有两种方式，一种是通过接口的class获取,如下:
```java
    ICheckApple checkApple = InterStellar.getLocalService(ICheckApple.class);
```
还有一种方法是通过接口的全路径名获取，如下:
```java
    ICheckApple checkApple =  InterStellar.getLocalService(ICheckApple.class.getCanonicalName());
```
与注册类似，仍然不推荐使用如下方式来获取，除非双方始终协商好使用一个唯一的key（但是这样对于新的调用方或者新加入的开发者不友好，容易入坑):
```java
    ICheckApple checkApple = InterStellar.getLocalService("wang.imallen.blog.moduleexportlib.apple.ICheckApple");
```
具体使用，可以察看applemodule中LocalServiceDemo这个Activity。

### 本地接口的Callback问题
如果是耗时操作，由本地接口自己定义Callback接口，调用方在调用接口时传入Callback对象即可。

## 远程服务的注册与使用
远程服务的注册与使用略微麻烦一点，因为需要像实现AIDL Service那样定义aidl接口。
### 远程接口的定义与实现
依靠@in,@out,@inout,@oneway这4个注解定义可进行IPC通信的接口，这四个注解分别对应于aidl中的in,out,inout和oneway修饰符。
函数参数不添加注解的话，默认为@in.
如下是一个典型的接口定义:
```java
   public interface IAppleService {

       int getApple(int money);

       float getAppleCalories(int appleNum);

       String getAppleDetails(int appleNum,  String manifacture,  String tailerName, String userName,  int userId);

       @oneway
       void oneWayTest(Apple apple);

       String outTest1(@out Apple apple);

       String outTest2(@out int[] appleNum);

       String outTest3(@out int[] array1, @out String[] array2);

       String outTest4(@out Apple[] apples);

       String inoutTest1(@inout Apple apple);

       String inoutTest2(@inout Apple[] apples);

   }
```


而接口的实现跟普通接口基本一样，除了要为@out和@inout的参数赋值之外:
```java
public class AppleService implements IAppleService {

    @Override
    public int getApple(int money) {
        return money / 2;
    }

    @Override
    public float getAppleCalories(int appleNum) {
        return appleNum * 5;
    }

    @Override
    public String getAppleDetails(int appleNum, String manifacture, String tailerName, String userName, int userId) {
        manifacture = "IKEA";
        tailerName = "muji";
        userId = 1024;
        if ("Tom".equals(userName)) {
            return manifacture + "-->" + tailerName;
        } else {
            return tailerName + "-->" + manifacture;
        }
    }

    @Override
    public synchronized void oneWayTest(Apple apple) {
        if(apple==null){
            Logger.d("Man can not eat null apple!");
        }else{
            Logger.d("Start to eat big apple that weighs "+apple.getWeight());
            try{
                wait(3000);
                //Thread.sleep(3000);
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
            Logger.d("End of eating apple!");
        }
    }

    @Override
    public String outTest1(Apple apple) {
        if (apple == null) {
            apple = new Apple(3.2f, "Shanghai");
        }
        apple.setWeight(apple.getWeight() * 2);
        apple.setFrom("Beijing");
        return "Have a nice day!";
    }

    @Override
    public String outTest2(int[] appleNum) {
        if (null == appleNum) {
            return "";
        }
        for (int i = 0; i < appleNum.length; ++i) {
            appleNum[i] = i + 1;
        }
        return "Have a nice day 02!";
    }

    @Override
    public String outTest3(int[] array1, String[] array2) {
        for (int i = 0; i < array1.length; ++i) {
            array1[i] = i + 2;
        }
        for (int i = 0; i < array2.length; ++i) {
            array2[i] = "Hello world" + (i + 1);
        }

        return "outTest3";
    }

    @Override
    public String outTest4(Apple[] apples) {
        for (int i = 0; i < apples.length; ++i) {
            apples[i] = new Apple(i + 2f, "Shanghai");
        }

        return "outTest4";
    }

    @Override
    public String inoutTest1(Apple apple) {
        Logger.d("AppleService-->inoutTest1,apple:" + apple.toString());
        apple.setWeight(3.14159f);
        apple.setFrom("Germany");
        return "inoutTest1";
    }

    @Override
    public String inoutTest2(Apple[] apples) {
        Logger.d("AppleService-->inoutTest2,apples[0]:" + apples[0].toString());
        for (int i = 0; i < apples.length; ++i) {
            apples[i].setWeight(i * 1.5f);
            apples[i].setFrom("Germany" + i);
        }
        return "inoutTest2";
    }
}

```


### 远程服务的注册
与本地接口的注册略有不同，远程接口注册的是继承了Stub类的IBinder部分，注册方式有传递接口Class和接口全路径名两种，如下:
```java
    InterStellar.registerRemoteService(IAppleService.class,new AppleService());
```

### 远程服务的使用
+ 由于InterStellar利用bindService()来提升通信过程中的优先级，对于在Fragment或者Activity中使用的情形，可在onDestroy()时自动释放连接，所以需要调用先调用with();
+ 由于跨进程只能传递IBinder,所以只能获取到远程服务的IBinder之后，再调用XX.Stub.asInterface()获取到它的代理.

以FragmentActivity中使用为例,如下的this是指FragmentActivity:
```java
        IAppleService appleService = InterStellar.with(BananaActivity.this).getRemoteService(IAppleService.class);
        if (appleService != null) {
            int appleNum = appleService.getApple(120);
            Logger.d("appleNum:" + appleNum);
            float calories = appleService.getAppleCalories(30);
            Logger.d("calories:" + calories);

            Apple apple = new Apple(1.3f, "Guangzhou");
            String desc = appleService.outTest1(apple);
            Logger.d("now apple:" + apple.toString());

        }
```
其他的在android.app.Fragment,android.support.v4.app.Fragment，以及普通的Activity中远程服务的使用类似，可查看app module中的CustomFragment,CustomSupportFragment,FragActivity等，不再赘述。

值得注意的是，**远程服务其实既可在其他进程中调用，也可以在同进程中被调用，当在同一进程时，虽然调用方式一样，但其实会自动降级为进程内普通的接口调用，这个binder会自动处理.**


### 生命周期自动管理的问题
对于IPC,为了提高对方进程的优先极，在使用InterStellar.with().getRemoteService()时会进行bindService()操作。
既然进行了bind操作，那自然要进行unbind操作以释放连接了，目前有如下两种情形。
+ 对于在Fragment或者Activity中，并且是在主线程中调用的情形，只要在获取远程服务时利用with()传递Fragment或者Activity对象进去，InterStellar就会在onDestroy()时自动释放连接，不需要开发者做任何unbind()操作。

+ 对于在子线程或者不是Fragment/Activity中的情形，只能传递Application Context去获取远程服务。在使用完毕后，需要手动调用InterStellar的unbind()释放连接:

```java
    public static void unbind(Class<?> serviceClass);
    public static void unbind(Set<Class<?>> serviceClasses);
```
  如果只获取了一个远程服务，那么就使用前一个unbind()方法;否则使用后一个。


# License
BSD-3-Clause. See the [BSD-3-Clause](https://opensource.org/licenses/BSD-3-Clause) file for details.

# TODO List
+ 远程服务的Callback
+ 事件总线


# 支持
1. Sample代码
2. 阅读Wiki或者FAQ
3. 联系bettarwang@gmail.com