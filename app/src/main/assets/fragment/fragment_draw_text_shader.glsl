precision mediump float;
varying vec2 f_texturePos;
uniform sampler2D textureId;

void main() {
    gl_FragColor = texture2D(textureId, f_texturePos);
}
