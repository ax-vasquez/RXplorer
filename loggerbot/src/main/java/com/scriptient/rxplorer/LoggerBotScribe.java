package com.scriptient.rxplorer;

import android.os.Build;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * LoggerBotScribe is an assistant class to LoggerBot - it's primary purpose is to provide crucial
 * data-gathering operations using the Reflection API and provide the data back to the caller to
 * be logged to the app database.
 *
 * @see <a href="https://docs.oracle.com/javase/tutorial/reflect/index.html">The Reflection API</a>
 * @see <a href="https://docs.oracle.com/javase/tutorial/reflect/class/index.html">Reflection API - Classes</a>
 * @see <a href="https://docs.oracle.com/javase/tutorial/reflect/member/index.html">Reflection API - Members</a>
 */
public class LoggerBotScribe {

    /**
     * Helper method to get an array of all methods defined in the provided class
     *
     * @param aClass                The class to get the methods from
     * @return                      Array of all methods defined in the passed-in class
     */
    private Method[] _retrieveMethodsForClass( Class aClass ) {

        Method[] classMethods;
        return classMethods = aClass.getMethods();

    }

    /**
     * Helper method to retrieve all parameters for the given method
     * <p>
     *     Per Oracle's documentation, the JVM <b>will not detect parameters unless you pass {@code -parameters}</b>
     *     in the command line when running the application. Otherwise, parameters are not populated.
     * <p>
     *     WARNING: As of 8/20/2018, this method is only capable of detecting parameters on API level
     *     26+. Still need to find a way to get parameters on a passed-in method when on an API level
     *     lower than 26 (ideally, API level 21 should be sufficient). This may not be possible, but
     *     need some time to look into this.
     *
     * @param method                The method to get the parameters for
     * @return                      The array of parameters for the method (unless on API level 25 or lower - in that case, null is returned)
     * @see <a href="https://docs.oracle.com/javase/tutorial/reflect/member/methodparameterreflection.html">Method Parameter Reflection</a>
     */
    private Parameter[] _retrieveParametersForMethod( Method method ) {

        Parameter[] methodParameters;

        // getParameters() only works on API level 26+ (Oreo)
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            return methodParameters = method.getParameters();
        } else
            return null;    // TODO: Find a workaround for pre-API level 26

    }

}
