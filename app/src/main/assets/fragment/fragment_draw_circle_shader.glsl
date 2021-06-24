precision mediump float;
varying vec4 v_texturePos;
varying vec4 f_textureColor;

void main() {
    if (f_textureColor==vec4(1.0, 0.0, 0.0, 1.0)){
        gl_FragColor = f_textureColor;
    } else {
        gl_FragColor = f_textureColor;
    }
}
