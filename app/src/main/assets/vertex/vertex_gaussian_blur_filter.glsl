attribute vec4 position;
attribute vec4 inputTextureCoordinate;

const int GAUSSIAN_SAMPLES = 9;

uniform float texelWidthOffset;
uniform float texelHeightOffset;

varying vec2 textureCoordinate;
varying vec2 blurCoordinates[GAUSSIAN_SAMPLES];

void main() {
    gl_Position = position;
    textureCoordinate = inputTextureCoordinate.xy;

    int multiplier = 0;
    vec2 blurStep;
    vec2 singleStepOffset = vec2(texelHeightOffset, texelWidthOffset);
    for (int i =0;i<GAUSSIAN_SAMPLES;i++){
        multiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));
        blurStep = float(multiplier) * singleStepOffset;
        blurCoordinates[i] = inputTextureCoordinate.xy + blurStep;
    }
}
