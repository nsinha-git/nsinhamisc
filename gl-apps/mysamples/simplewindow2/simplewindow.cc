#include <stdlib.h>
#include <stdio.h>
#include <GL/glew.h>
//#include <GL/gl3w.h>
#include <glfw3.h>
//#include <GL/glx.h>
#include <math.h>
#include "sb6.h"
#include "shader.hpp"

GLuint compile_shaders(void) {
	GLuint vert_shader, frag_shader, prog;
	static const GLchar * vert_shader_src[] ={
		"#version 330 core \n"
		"layout(location = 0) in vec3 vertexPosition_modelspace;\n"
		" void main(){\n"
		" gl_Position.xyz = vertexPosition_modelspace;\n"
	    	" gl_Position.w = 1.0;\n"
		"}\n"
	};


	static const GLchar * vert_shader_src1[] ={
		"#version 330 core \n"
		"void main(void) { \n"
		"	const vec4 verts[3] = vec4[3](vec4(0.25, -0.25, 0.5,1.0),\n"
		" vec4(-0.25, -0.25, 0.5,1.0),  \n"
		" vec4(0.25, 0.25, 0.5,1.0)); \n"
		"	gl_Position = verts[gl_VertexID]; \n"
		"} \n"
	};

	static const GLchar * frag_shader_src[] ={
		"#version 330 core \n"
		"out vec3 color;\n"
		"void main() { \n"
                " color = vec3(1,0,0);\n"
		"}\n"
	};
     

	static const GLchar * frag_shader_src1[] ={
		"#version 330 core \n"
		"out vec4 color; \n"
		"void main(void) { \n"
		"	color = vec4(0.0,0.8,1.0, 1.0); \n"
		"} \n"
	};


	vert_shader = glCreateShader(GL_VERTEX_SHADER);
	glShaderSource(vert_shader, 1, vert_shader_src,NULL);
	glCompileShader(vert_shader);


	frag_shader = glCreateShader(GL_FRAGMENT_SHADER);
	glShaderSource(frag_shader, 1, frag_shader_src,NULL);
	glCompileShader(frag_shader);


	prog = glCreateProgram();
	glAttachShader(prog, vert_shader);
	glAttachShader(prog, frag_shader);
	glLinkProgram(prog);


	glDeleteShader(vert_shader);
	glDeleteShader(frag_shader);
}


class my_app: public application {
public:
	void startup() {
		glGenVertexArrays(1, &vertexArrayID);
		glBindVertexArray(vertexArrayID);
		rend_prog = compile_shaders();
		//programID = LoadShaders( "SimpleVertexShader.vertexshader", "SimpleFragmentShader.fragmentshader" );
		static const GLfloat g_vertex_buffer_data[] = { 
                -1.0f, -1.0f, 0.0f,
                 1.0f, -1.0f, 0.0f,
                 0.0f,  1.0f, 0.0f,
        };
		glGenBuffers(1, &vertexbuffer);
		glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
		glBufferData(GL_ARRAY_BUFFER, sizeof(g_vertex_buffer_data), g_vertex_buffer_data, GL_STATIC_DRAW);
	}
	void shutdown() {
		glDeleteVertexArrays(1,&vertexArrayID);
		glDeleteProgram(rend_prog);
		glDeleteVertexArrays(1,&vertexArrayID);
	}

        void render(double ctime) {
                GLfloat red[] = {(float)sin(ctime), (float)cos(ctime), 0.0f, 1.0f};
                glClear( GL_COLOR_BUFFER_BIT );

		glUseProgram(rend_prog);
		//glUseProgram(programID);

                glClearBufferfv(GL_COLOR, 0, red);
		glEnableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
		glVertexAttribPointer(
                        0,                  // attribute 0. No particular reason for 0, but must match the layout in the shader.
                        3,                  // size
                        GL_FLOAT,           // type
                        GL_FALSE,           // normalized?
                        0,                  // stride
                        (void*)0            // array buffer offset
                );


		glDrawArrays(GL_TRIANGLES, 0,3);
		glDisableVertexAttribArray(0);
		glfwSwapBuffers(window);
		glfwPollEvents();
        }
private:
	GLuint rend_prog, programID;
	GLuint vertexbuffer; 
	GLuint vertexArrayID; 

};

DECLARE_MAIN(my_app);

