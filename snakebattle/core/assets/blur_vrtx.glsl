attribute vec4 a_position;

uniform mat4 u_projectionViewMatrix;

void main()
{
    gl_Position = a_position * u_projectionViewMatrix;
}
