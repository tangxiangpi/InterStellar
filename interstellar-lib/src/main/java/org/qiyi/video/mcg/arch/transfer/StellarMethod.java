package org.qiyi.video.mcg.arch.transfer;

import android.os.Parcel;
import android.os.Parcelable;

import org.qiyi.video.mcg.arch.annotation.oneway;
import org.qiyi.video.mcg.arch.log.Logger;
import org.qiyi.video.mcg.arch.param.AbstractParameter;
import org.qiyi.video.mcg.arch.param.InOutParameter;
import org.qiyi.video.mcg.arch.param.InParameter;
import org.qiyi.video.mcg.arch.param.OutParameter;
import org.qiyi.video.mcg.arch.parcelable.Outable;
import org.qiyi.video.mcg.arch.type.TransferType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;


/**
 * Created by wangallen on 2018/3/31.
 */

public class StellarMethod implements Parcelable, Outable {

    private String methodName;

    //TODO 其实returnType好像是不需要的，因为方法重载是不care返回值的，真正需要的是参数类型
    //TODO 还是需要的，不然不知道按照何种规则进行序列化和反序列化
    //private StellarParameter returnType;

    //private StellarParameter[] parameters;
    private AbstractParameter[] parameters;

    private boolean isOneWay = false;

    public static final Creator<StellarMethod> CREATOR = new Creator<StellarMethod>() {
        @Override
        public StellarMethod createFromParcel(Parcel source) {
            return new StellarMethod(source);
        }

        @Override
        public StellarMethod[] newArray(int size) {
            return new StellarMethod[size];
        }
    };

    public StellarMethod(Method method, Object[] args) {
        this.methodName = method.getName();
        parseMethodAnnotation(method.getAnnotations());
        //TODO 这样不好，实际上returnType只要用一个int数表示即可
        if (null == args || args.length < 1) {
            return;
        }
        Annotation[][] paraAnnos = method.getParameterAnnotations();
        parameters = new AbstractParameter[args.length];
        Type[] genericParaTypes = method.getGenericParameterTypes();
        for (int i = 0; i < args.length; ++i) {
            int transferType = getTransferType(paraAnnos[i]);
            if (transferType == TransferType.IN) {
                parameters[i] = new InParameter(args[i], genericParaTypes[i]);
            } else if (transferType == TransferType.OUT) {
                parameters[i] = new OutParameter(args[i], genericParaTypes[i]);
            } else {
                parameters[i] = new InOutParameter(args[i], genericParaTypes[i]);
            }
            //parameters[i] = new StellarParameter(args[i], paraAnnos[i], genericParaTypes[i]);
        }
    }

    private int getTransferType(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof org.qiyi.video.mcg.arch.annotation.in) {
                return TransferType.IN;
            } else if (annotation instanceof org.qiyi.video.mcg.arch.annotation.out) {
                return TransferType.OUT;
            } else if (annotation instanceof org.qiyi.video.mcg.arch.annotation.inout) {
                return TransferType.INOUT;
            }
        }
        return TransferType.IN;
    }

    private void parseMethodAnnotation(Annotation[] annotations) {
        for (Annotation anno : annotations) {
            if (anno instanceof oneway) {
                isOneWay = true;
                break;
            }
        }
    }

    public StellarMethod(Parcel in) {
        this.methodName = in.readString();
        int length=in.readInt();
        if(length<0){
            return;
        }
        parameters= (AbstractParameter[])Array.newInstance(AbstractParameter.class,length);
        for(int i=0;i<length;i++){
            parameters[i]=in.readParcelable(getClass().getClassLoader()); //TODO 目前OutParameter在这里会出错
        }
        Logger.d("StellarMethod-->StellarMethod(Parcel),readParcelableArray");
    }

    //TODO writeArgsToParcel(..)和dest.writeParcelableArray(..)有什么区别呢？本质上是一样的吗？这里有没有更简单一点的做法呢?
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(methodName);
        if(parameters==null){
            dest.writeInt(-1);
        }else{
            dest.writeInt(parameters.length);
            for(int i=0;i<parameters.length;i++){
                //dest.writeParcelable(parameters[i],flags);
                //KP writeToParcel和直接dest.writeParcelable(..)的区别在于，前者会让我们自己定义的OutParameter根据flags来决定是写入值还是只写入类型
                if(flags==PARCELABLE_WRITE_RETURN_VALUE){
                    parameters[i].writeToParcel(dest,flags);
                }else{
                    dest.writeParcelable(parameters[i],flags);
                }
            }
        }
    }

    @Override
    public void readFromParcel(Parcel in) {
        Logger.d("StellarMethod-->readFromParcel");
        this.methodName = in.readString();
        readFromParcel4Parameters(in,getClass().getClassLoader());
    }
    //TODO 这样写是不是不对呢?parameters是什么时候创建的呢?
    private void readFromParcel4Parameters(Parcel in,ClassLoader loader) {
        int N = in.readInt();
        if (N < 0) {
            return;
        }
        for (int i = 0; i < N; i++) {
            //parameters[i]=in.readParcelable(loader);
            parameters[i].readFromParcel(in);

            /*
            if (parameters[i] instanceof Outable) {
                //调用下面这个语句的时候，虽然能够执行成功，但是参数却没有被改变
                //parameters[i]=in.readParcelable(loader);
                //TODO 注意要跟写入保持一致，如果写入是parameter[i].writeToParcel(..);那这里确实要用para.readFromParcel();但现在写入时用的是dest.writeParcelable(..);所以读取时就要用dest.readParcelable(..);
                Outable para = (Outable) parameters[i];
                para.readFromParcel(in);
                Logger.d("StellarMethod-->readFromParcel4Parameters,parameters["+i+"]:"+parameters[i].toString());
            }else{
                parameters[i]=in.readParcelable(loader);
            }
            */
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void writeArgsToParcel(Parcel dest, int flags) {
        Logger.d("StellarMethod-->writeArgsToParcel()");
        if (null == parameters) {
            dest.writeInt(-1);
        } else {
            dest.writeInt(parameters.length);
            for (AbstractParameter param : parameters) {
                param.writeToParcel(dest, flags);
            }
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public AbstractParameter[] getParameters() {
        return parameters;
    }

    public boolean isOneWay() {
        return isOneWay;
    }
}
