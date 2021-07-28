attribute vec4 position;
attribute vec4 inputTextureCoordinate;

uniform mat4 transMatrix;
uniform mat4 rotateMatrix;
uniform mat4 orthographicMatrix;

varying vec2 textureCoordinate;

void main() {
    gl_Position = transMatrix * rotateMatrix * vec4(position.xyz, 1.0) * orthographicMatrix;
    textureCoordinate = inputTextureCoordinate.xy;
}
