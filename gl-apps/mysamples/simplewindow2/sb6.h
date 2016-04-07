#ifndef __SB6_H__
#define __SB6_H__
#include <stdio.h>
#include <string.h>
#include <GL/glew.h>
#include <glfw3.h>
#include <glm/glm.hpp>



class application
{
public:
    application() {}
    virtual ~application() {}
    virtual void run()
    {
        if (!glfwInit())
        {
            fprintf(stderr, "Failed to initialize GLFW\n");
            return;
        }
	glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE); // To make MacOS happy; should not be needed
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(info.windowWidth, info.windowHeight,info.title ,NULL, NULL);
	if( window == NULL ){
                fprintf( stderr, "Failed to open GLFW window. If you have an Intel GPU, they are not 3.3 compatible. Try the 2.1 version of the tutorials.\n" );
                getchar();
                glfwTerminate();
                return ;
        }
	glfwMakeContextCurrent(window);
	glewExperimental = true; // Needed for core profile
	if (glewInit() != GLEW_OK) {
                fprintf(stderr, "Failed to initialize GLEW\n");
                getchar();
                glfwTerminate();
                return ;
        }

	glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE);

        glClearColor(0.0f, 0.0f, 0.4f, 0.0f);
        

        startup();

        do
        {
            render(glfwGetTime());

            glfwSwapBuffers(window);

            running &= (glfwGetKey(window, GLFW_KEY_ESCAPE ) == GLFW_RELEASE);
        } while(running);

        shutdown();

        glfwTerminate();
    }

    virtual void init()
    {
        strcpy(info.title, "SuperBible6 Example");
        info.windowWidth = 800;
        info.windowHeight = 600;
#ifdef __APPLE__
        info.majorVersion = 3;
        info.minorVersion = 2;
#else
        info.majorVersion = 3;
        info.minorVersion = 3;
#endif
        info.samples = 4;
        info.flags.all = 0;
        info.flags.cursor = 1;
#ifdef _DEBUG
        info.flags.debug = 1;
#endif
    }

    virtual void startup()
    {

    }

    virtual void render(double currentTime)
    {

    }

    virtual void shutdown()
    {

    }

    virtual void onResize(int w, int h)
    {
        info.windowWidth = w;
        info.windowHeight = h;
    }

    virtual void onKey(int key, int action)
    {

    }

    virtual void onMouseButton(int button, int action)
    {

    }

    virtual void onMouseMove(int x, int y)
    {

    }

    virtual void onMouseWheel(int pos)
    {

    }

    virtual void onDebugMessage(GLenum source,
                                GLenum type,
                                GLuint id,
                                GLenum severity,
                                GLsizei length,
                                const GLchar* message)
    {
#ifdef _WIN32
        OutputDebugStringA(message);
        OutputDebugStringA("\n");
#endif /* _WIN32 */
    }

    static void getMousePosition(int& x, int& y)
    {
        //glfwGetMousePos(&x, &y);
    }

public:
    struct APPINFO
    {
        char title[128];
        int windowWidth;
        int windowHeight;
        int majorVersion;
        int minorVersion;
        int samples;
        union
        {
            struct
            {
                unsigned int    fullscreen  : 1;
                unsigned int    vsync       : 1;
                unsigned int    cursor      : 1;
                unsigned int    stereo      : 1;
                unsigned int    debug       : 1;
            };
            unsigned int        all;
        } flags;
    };

protected:
    APPINFO     info;
    GLFWwindow* window;
    bool running = true;

    void  glfw_onResize(GLFWwindow* window, int w, int h)
    {
        this->onResize(w, h);
    }

    void  glfw_onKey(GLFWwindow* window, int key, int action, int p_1, int p_2)
    {
        this->onKey(key, action);
    }

    void  glfw_onMouseButton(GLFWwindow* window, int button, int action,int p_1)
    {
        this->onMouseButton(button, action);
    }

    void  glfw_onMouseMove(GLFWwindow* window, double x, double y)
    {
        this->onMouseMove(x, y);
    }

    void  glfw_onMouseWheel(GLFWwindow* window, double pos, double p_1)
    {
        this->onMouseWheel(pos);
    }

    void setVsync(bool enable)
    {
        info.flags.vsync = enable ? 1 : 0;
        glfwSwapInterval((int)info.flags.vsync);
    }

};


#define DECLARE_MAIN(a)                             \
int main(int argc, const char ** argv)              \
{                                                   \
    a *app = new a;                                 \
    app->run();                                  \
    delete app;                                     \
    return 0;                                       \
}

#endif /* __SB6_H__ */

