precision mediump float;
varying vec2 f_texturePos;
uniform sampler2D textureId;

void main() {
    vec4 sampleColor = texture2D(textureId, f_texturePos);
    if (sampleColor == vec4(1.0, 0.0, 0.0, 1.0)){
        gl_FragColor = vec4(sampleColor.rgb, abs(f_texturePos.x));
    } else {
        gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
    }
}
