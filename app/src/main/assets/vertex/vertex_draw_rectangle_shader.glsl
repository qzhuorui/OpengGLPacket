attribute vec4 av_Position;
attribute vec4 af_Position;
uniform mat4 u_Matrix;
varying vec4 f_texturePos;

void main() {
    gl_Position = u_Matrix * av_Position;
    f_texturePos = af_Position;
}
