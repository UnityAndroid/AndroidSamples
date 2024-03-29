 package com.positive.mbit.intel.android.graphics;
 
 import android.opengl.GLES20;
 import android.util.Log;

 import com.positive.mbit.intel.domain.graphics.IEglUtil;
 import com.positive.mbit.intel.domain.graphics.IShaderProgram;
 import java.util.HashMap;
 
 public class ShaderProgram
   implements IShaderProgram
 {
   private static int INVALID_VALUE = -1;
   private int programHandle = INVALID_VALUE;
   private HashMap<String, Integer> attributes = new HashMap();
   private IEglUtil eglUtil;
 
   public ShaderProgram(IEglUtil eglUtil)
   {
     this.eglUtil = eglUtil;
   }
 
   public void create(String vertexCode, String fragmentCode) {

     Log.e("shader","vertex fragment");
     int vertexShader = INVALID_VALUE;
     if (vertexCode != null) {
       vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexCode);
     }
     if (vertexShader == 0) {
       this.programHandle = 0;
       return;
     }

     Log.e("shader","vertex");

     int fragmentShader = INVALID_VALUE;
     if (fragmentCode != null) {
       fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentCode);
     }
     if (fragmentShader == 0) {
       this.programHandle = 0;
       return;
     }

     Log.e("shader","fragment");
     this.programHandle = GLES20.glCreateProgram();
     this.eglUtil.checkEglError("glCreateProgram");
 
     GLES20.glAttachShader(this.programHandle, vertexShader);
     this.eglUtil.checkEglError("glAttachShader");
 
     GLES20.glAttachShader(this.programHandle, fragmentShader);
     this.eglUtil.checkEglError("glAttachShader");
 
     GLES20.glLinkProgram(this.programHandle);
   }
 
   public void use() {
     GLES20.glUseProgram(this.programHandle);
   }
 
   public void unUse() {
     GLES20.glUseProgram(0);
   }
 
   private int loadShader(int type, String shaderCode) {
     int shader = GLES20.glCreateShader(type);
     this.eglUtil.checkEglError("glCreateShader");
 
     GLES20.glShaderSource(shader, shaderCode);
     this.eglUtil.checkEglError("glShaderSource");
 
     GLES20.glCompileShader(shader);
     this.eglUtil.checkEglError("glCompileShader");
 
     int[] status = new int[1];
     GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);
     if (status[0] == 0) {
       String info = GLES20.glGetShaderInfoLog(shader);
       GLES20.glDeleteShader(shader);
       throw new IllegalArgumentException(" Shader compilation failed with: " + info);
     }
 
     return shader;
   }
 
   public int getAttributeLocation(String attribute) {
     if (this.attributes.containsKey(attribute)) {
       return ((Integer)this.attributes.get(attribute)).intValue();
     }
     int location = GLES20.glGetAttribLocation(this.programHandle, attribute);
     this.eglUtil.checkEglError("glGetAttribLocation " + attribute);
     if (location == -1) {
       location = GLES20.glGetUniformLocation(this.programHandle, attribute);
       this.eglUtil.checkEglError("glGetUniformLocation " + attribute);
     }
     if (location == -1) {
       throw new IllegalStateException("Can't find a location for attribute " + attribute);
     }
     this.attributes.put(attribute, Integer.valueOf(location));
     return location;
   }
 
   public int getProgramHandle() {
     return this.programHandle;
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\android-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.android.graphics.ShaderProgram
 * JD-Core Version:    0.6.1
 */