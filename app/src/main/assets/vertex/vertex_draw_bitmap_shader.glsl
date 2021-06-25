attribute vec4 av_Position;
attribute vec2 af_Position;
attribute vec2 af_textSize;

varying vec2 f_texturePos;
varying vec2 f_textureSize;


void main() {
    gl_Position =  av_Position;
    f_texturePos = af_Position;
    f_textureSize = af_textSize;
}
