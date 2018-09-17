package com.skateboard.corronisonview

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES30.*
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.util.Log
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CorronisonViewRender(val context: Context, val bitmap: Bitmap) : GLSurfaceView.Renderer
{

    val TAG = "CORRONISONVIEWRENDER"

    private var programId = 0

    private var VAO = 0

    private var POSVBO = 0

    private var TEXVBO = 0

    private var textureId = 0

    var percent = 0.0f

    private val pos = floatArrayOf(

            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,

            1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f
    )

    private val texCoord = floatArrayOf(

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,

            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    )

    override fun onDrawFrame(gl: GL10?)
    {
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        glUseProgram(programId)
        val percentLocation = glGetUniformLocation(programId, "percent")
        glUniform1f(percentLocation, percent)
        glBindVertexArray(VAO)
        glDrawArrays(GL_TRIANGLES, 0, 6)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int)
    {
        glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?)
    {
        val vertexSource = loadSlglSource("vertex.slgl")
        val fragmentSource = loadSlglSource("fragment.slgl")
        programId = createProgram(vertexSource, fragmentSource)
        val vaoArray = IntArray(1)
        glGenVertexArrays(1, vaoArray, 0)
        VAO = vaoArray[0]
        glBindVertexArray(VAO)
        val vboArray = IntArray(2)
        glGenBuffers(2, vboArray, 0)
        POSVBO = vboArray[0]
        glBindBuffer(GL_ARRAY_BUFFER, POSVBO)
        val posBuffer = ByteBuffer.allocateDirect(4 * pos.size).order(ByteOrder.nativeOrder()).asFloatBuffer()
        posBuffer.put(pos)
        posBuffer.position(0)
        glBufferData(GL_ARRAY_BUFFER, 4 * pos.size, posBuffer, GL_STATIC_DRAW)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * 4, 0)
        glEnableVertexAttribArray(0)

        TEXVBO = vboArray[1]
        glBindBuffer(GL_ARRAY_BUFFER, TEXVBO)
        val texCoordBuffer = ByteBuffer.allocateDirect(4 * texCoord.size).order(ByteOrder.nativeOrder()).asFloatBuffer()
        texCoordBuffer.put(texCoord)
        texCoordBuffer.position(0)
        glBufferData(GL_ARRAY_BUFFER, 4 * texCoord.size, texCoordBuffer, GL_STATIC_DRAW)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 2 * 4, 0)
        glEnableVertexAttribArray(1)

        textureId = generateTextureId()
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)
    }

    private fun generateTextureId(): Int
    {
        val textureIdArray = IntArray(1)
        glGenTextures(1, textureIdArray, 0)
        val textureId = textureIdArray[0]
        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        return textureId
    }

    private fun loadSlglSource(path: String): String
    {
        val sb = StringBuffer()
        try
        {
            val reader = BufferedReader(InputStreamReader(context.assets.open(path)))
            var line = reader.readLine()
            while (line != null)
            {
                sb.append(line)
                sb.append("\n")
                line = reader.readLine()
            }
        } catch (e: FileNotFoundException)
        {
            e.printStackTrace()
        } catch (e: IOException)
        {
            e.printStackTrace()
        }
        return sb.toString()
    }

    private fun createProgram(vertexSource: String, fragmentSource: String): Int
    {
        val vertexShader = createShader(GL_VERTEX_SHADER, vertexSource)
        val fragmentShader = createShader(GL_FRAGMENT_SHADER, fragmentSource)
        return createProgram(vertexShader, fragmentShader)
    }


    private fun createShader(type: Int, source: String): Int
    {
        val shaderId = glCreateShader(type)
        if (shaderId <= 0)
        {
            Log.d(TAG, "create shader failed")
        }
        glShaderSource(shaderId, source)
        glCompileShader(shaderId)
        val status = IntArray(1)
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, status, 0)
        if (status[0] <= 0)
        {
            Log.d(TAG, "compile shader failed")
            val infoLog = glGetShaderInfoLog(shaderId)
            Log.d(TAG, infoLog)
            glDeleteShader(shaderId)
        }
        return shaderId
    }

    private fun createProgram(vertexShader: Int, fragmentShader: Int): Int
    {
        val programId = glCreateProgram()
        if (programId <= 0)
        {
            Log.d(TAG, "create program failed")
        }
        glAttachShader(programId, vertexShader)
        glAttachShader(programId, fragmentShader)
        glLinkProgram(programId)
        val status = IntArray(1)
        glGetProgramiv(programId, GL_LINK_STATUS, status, 0)
        if (status[0] <= 0)
        {
            Log.d(TAG, "link program failed")
            val infoLog = glGetProgramInfoLog(programId)
            Log.d(TAG, infoLog)
        }
        return programId
    }

}