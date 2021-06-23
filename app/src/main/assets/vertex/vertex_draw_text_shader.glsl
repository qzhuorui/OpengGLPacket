attribute vec4 av_Position;
attribute vec2 af_Position;
uniform mat4 transMatrix;
uniform mat4 rotateMatrix;
uniform mat4 orthographicMatrix;
varying vec2 f_texturePos;

void main() {
    gl_Position = rotateMatrix * av_Position;
    //    gl_Position = transMatrix * rotateMatrix * orthographicMatrix * vec4(av_Position.xyz, 1.0);
    //    gl_Position = av_Position;
    f_texturePos = af_Position;
}
