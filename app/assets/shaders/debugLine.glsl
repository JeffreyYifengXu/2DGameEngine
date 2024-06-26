#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aColour;

out vec3 fColour;

uniform mat4 uView;
uniform mat4 uProjection;

void main() {
    fColour = aColour;

    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}


#type fragment
#version 330 core
in vec3 fColour;

out vec4 colour;

void main() {
    colour = vec4(fColour, 1);
}