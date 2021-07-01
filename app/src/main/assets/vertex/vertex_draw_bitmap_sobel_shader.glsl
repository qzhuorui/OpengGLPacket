attribute vec4 av_Position;
attribute vec2 af_Position;
attribute vec2 af_textSize;

uniform mat4 ModelViewMatrix;
uniform mat4 MVP;

varying vec2 f_texturePos;
varying vec2 f_textureSize;

varying vec3 Position;


void main() {
    f_texturePos = af_Position;
    Position = vec3(ModelViewMatrix * av_Position);
    gl_Position = MVP * av_Position;
    f_textureSize = af_textSize;
}
