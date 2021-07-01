precision mediump float;
varying vec2 f_texturePos;
varying vec2 f_textureSize;
uniform sampler2D textureId;


void main() {
    gl_FragColor = texture2D(textureId, f_texturePos);
}
