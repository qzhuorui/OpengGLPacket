uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;

varying highp vec2 textureCoordinate;
varying highp vec2 textureCoordinate2;

uniform lowp float mixturePercent;

void main() {
    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);
    lowp vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);

    lowp vec4 outputColor;
    outputColor.r = textureColor.r + textureColor2.r * textureColor2.a * (1.0 - textureColor.a);
    outputColor.g = textureColor.g + textureColor2.g * textureColor2.a * (1.0 - textureColor.a);
    outputColor.b = textureColor.b + textureColor2.b * textureColor2.a * (1.0 - textureColor.a);
    outputColor.a = textureColor.a + textureColor2.a * (1.0 - textureColor.a);
    gl_FragColor = outputColor;
}
