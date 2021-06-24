attribute vec4 av_Position;
attribute vec4 af_Color;
uniform mat4 u_Matrix;
varying vec4 v_texturePos;
varying vec4 f_textureColor;

void main() {
    v_texturePos = u_Matrix * av_Position;
    gl_Position = v_texturePos;
    f_textureColor = af_Color;
}
