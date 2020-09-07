package org.jackie.engine.opengles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import com.example.nest.R;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SceneRenderer implements GLSurfaceView.Renderer {
    private int textureId;//系统分配的纹理id

    float yAngle;//绕Y轴旋转的角度
    float xAngle; //绕Z轴旋转的角度
    //从指定的obj文件中加载对象
    private LoadedObjectVertexNormalTexture lovo;

    private MySurfaceView mv;

    SceneRenderer(MySurfaceView mv) {
        this.mv = mv;
    }

    public void onDrawFrame(GL10 gl) {
        //清除深度缓冲与颜色缓冲
        GLES31.glClear(GLES31.GL_DEPTH_BUFFER_BIT | GLES31.GL_COLOR_BUFFER_BIT);

        //坐标系推远
        MatrixState.pushMatrix();
        MatrixState.translate(0, -16f, -60f);   //ch.obj
        //绕Y轴、Z轴旋转
        MatrixState.rotate(yAngle, 0, 1, 0);
        MatrixState.rotate(xAngle, 1, 0, 0);

        MatrixState.autoRotate();

        //若加载的物体部位空则绘制物体
        if (lovo != null) {
            lovo.drawSelf(textureId);
        }
        MatrixState.popMatrix();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视窗大小及位置
        GLES31.glViewport(0, 0, width, height);
        //计算GLSurfaceView的宽高比
        float ratio = (float) width / height;
        //调用此方法计算产生透视投影矩阵
        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
        //调用此方法产生摄像机9参数位置矩阵
        MatrixState.setCamera(0, 0.5f, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //设置屏幕背景色RGBA
        GLES31.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //打开深度检测
        GLES31.glEnable(GLES31.GL_DEPTH_TEST);
        //打开背面剪裁
        GLES31.glEnable(GLES31.GL_CULL_FACE);
        //初始化变换矩阵
        MatrixState.setInitStack();
        //初始化光源位置
        MatrixState.setLightLocation(40, 10, 20);
        //加载要绘制的物体
        lovo = LoadUtil.loadFromFile(mv, mv.getResources().openRawResource(R.raw.warm_house));
        //加载纹理
        textureId = initTexture(R.mipmap.sea);
    }

    private int initTexture(int drawableId)//textureId
    {
        //生成纹理ID
        int[] textures = new int[1];
        GLES31.glGenTextures
                (
                        1,          //产生的纹理id的数量
                        textures,   //纹理id的数组
                        0           //偏移量
                );
        int textureId = textures[0];
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureId);
        GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_NEAREST);
        GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR);
        GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_REPEAT);
        GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_REPEAT);

        //通过输入流加载图片===============begin===================
        InputStream is = mv.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try {
            bitmapTmp = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //通过输入流加载图片===============end=====================
        GLUtils.texImage2D
                (
                        GLES31.GL_TEXTURE_2D, //纹理类型
                        0,
                        GLUtils.getInternalFormat(bitmapTmp),
                        bitmapTmp, //纹理图像
                        GLUtils.getType(bitmapTmp),
                        0 //纹理边框尺寸
                );
        bitmapTmp.recycle();          //纹理加载成功后释放图片
        return textureId;
    }
}