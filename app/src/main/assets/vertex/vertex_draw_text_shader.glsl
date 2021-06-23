attribute vec4 av_Position;
attribute vec2 af_Position;
varying vec2 f_texturePos;

void main() {
    gl_Position = av_Position;
    f_texturePos = af_Position;
}
