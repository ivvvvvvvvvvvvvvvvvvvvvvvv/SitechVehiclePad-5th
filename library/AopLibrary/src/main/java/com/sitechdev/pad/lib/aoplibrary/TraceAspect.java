package com.sitechdev.pad.lib.aoplibrary;

import android.text.TextUtils;
import android.util.Log;
import android.widget.RadioButton;

import com.sitechdev.pad.lib.aoplibrary.util.classWatch;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 项目名称：xinte_8
 * 类名称：TraceAspect
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/04/04 0004 11:46
 * 修改时间：
 * 备注：
 */
@Aspect
public class TraceAspect {

    private static final String TAG = "AspectJ";
    private static final String POINTCUT_METHOD =
            "execution(@com.sitechdev.pad.lib.aoplibrary.annotation.DebugTrace * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(@com.sitechdev.pad.lib.aoplibrary.annotation.DebugTrace *.new(..))";
    private long lastTouchSoundTime = 0L;

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithDebugTrace() {
    }

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorAnnotatedDebugTrace() {
    }

//    @Around("execution(* android.widget.RadioGroup.OnCheckedChangeListener.onCheckedChanged(..))|| execution(* android.view.View.OnClickListener.onClick(..))|| execution(* android.widget.CompoundButton.OnCheckedChangeListener.onCheckedChanged(..))|| execution(* com.sitechdev.vehicle.utils.JumpUtils.jump(..))")
    @Around("execution(* android.view.View.OnClickListener.onClick(..))|| execution(* android.widget.CompoundButton.OnCheckedChangeListener.onCheckedChanged(..))|| execution(* com.sitechdev.vehicle.pad.utils.JumpUtils.jump(..))")
    public void onViewEventClick(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        Log.i(TAG, "onViewEventClick: joinPoint==>:==>" + key + "\n" + joinPoint.getThis());
        String staticPart = joinPoint.getStaticPart().getSignature().getName();
        boolean noRadioButton = true;
        if (TextUtils.equals("onClick", staticPart)) {
            Object[] args = joinPoint.getArgs();
            if (null != args){
                if (args[0] instanceof RadioButton){
                    noRadioButton = false;
                }
            }
        }

        if (noRadioButton && isFastMoreClick()) {
            return;
        }

//        //获取方法信息对象
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        //获取当前对象
//        String className = methodSignature.getDeclaringType().getSimpleName();
//        String methodName = methodSignature.getName();
//
//        Log.i(className, "onViewEventClick" + buildLogMessage(methodName, 1000));
//        if (joinPoint.getArgs().length > 0) {
//            View view = (View) joinPoint.getArgs()[0];
//            //xml中定义的View的ID
//            String viewId = "";
//            if (view.getId() != View.NO_ID) {
//                viewId = view.getResources().getResourceEntryName(view.getId());
//            }
//            Log.e(TAG, "onViewEventClick: viewId = " + viewId);
//        }

        try {
            //播放音效
//            EventBusUtils.postEvent(new SoundEvent(SoundEvent.EB_SOUND_PLAY_CLICK));
            //执行
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Around("methodAnnotatedWithDebugTrace() || constructorAnnotatedDebugTrace()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        //Log.i(TAG, "weaveJoinPoint:==>" + joinPoint.toLongString());
        //获取方法信息对象
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取当前对象
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        //获取当前对象，通过反射获取类别详细信息
        //String className2 = joinPoint.getThis().getClass().getName();

        //初始化计时器
        final classWatch stopWatch = new classWatch();
        //开始监听
        stopWatch.start();
        //调用原方法的执行。
        Object result = joinPoint.proceed();
        //监听结束
        stopWatch.stop();

        Log.i(className + "->methodTime", buildLogMessage(methodName, stopWatch.getTotalTimeMillis()));

        return result;
    }

    /**
     * Create a log message.
     *
     * @param methodName     A string with the method name.
     * @param methodDuration Duration of the method in milliseconds.
     * @return A string representing message.
     */
    private static String buildLogMessage(String methodName, long methodDuration) {
        StringBuilder message = new StringBuilder();
        message.append("--> ");
        message.append(methodName);
        message.append(" --> ");
        message.append("[");
        message.append(methodDuration);
        message.append("ms");
        message.append("]");

        return message.toString();
    }

    private boolean isFastMoreClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTouchSoundTime < 500) {
            Log.i(TAG, "======================>快速点击===>");
            lastTouchSoundTime = currentTime;
            return true;
        }
        lastTouchSoundTime = currentTime;
        return false;
    }
}
