attribute vec4 av_Position;
attribute vec4 af_Color;
uniform mat4 u_Matrix;

varying vec4 f_textureColor;

void main() {
    gl_Position =  u_Matrix * av_Position;
    f_textureColor = af_Color;
}
