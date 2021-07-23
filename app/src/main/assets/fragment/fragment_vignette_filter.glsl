uniform sampler2D inputImageTexture;
varying highp vec2 textureCoordinate;

uniform lowp vec2 vignetteCenter;
uniform lowp vec2 vignetteColor;
uniform highp float vignetteStart;
uniform highp float vignetteEnd;

void main() {
    lowp vec3 rgb = texture2D(inputImageTexture, textureCoordinate).rgb;
    lowp float distance = distance(textureCoordinate, vignetteCenter);
    rgb *= (1.0 - smoothstep(vignetteStart, vignetteEnd, distance));
    gl_FragColor = vec4(vec3(rgb), 1.0);

}
