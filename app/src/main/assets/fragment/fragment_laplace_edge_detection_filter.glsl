precision highp float;

varying highp vec2 textureCoordinate;
varying highp vec2 leftTextureCoordinate;
varying highp vec2 rightTextureCoordinate;
varying highp vec2 topTextureCoordinate;
varying highp vec2 bottomTextureCoordinate;

varying highp float centerMultiplier;
varying highp float edgeMultiplier;

uniform sampler2D inputImageTexture;

void main()
{
    mediump vec3 textureColor = texture2D(inputImageTexture, textureCoordinate).rgb;
    mediump vec3 leftTextureColor = texture2D(inputImageTexture, leftTextureCoordinate).rgb;
    mediump vec3 rightTextureColor = texture2D(inputImageTexture, rightTextureCoordinate).rgb;
    mediump vec3 topTextureColor = texture2D(inputImageTexture, topTextureCoordinate).rgb;
    mediump vec3 bottomTextureColor = texture2D(inputImageTexture, bottomTextureCoordinate).rgb;

    //4Z5 - (Z2 + Z4 + Z6 + Z8)
    vec3 color = textureColor * centerMultiplier - (leftTextureColor * edgeMultiplier + rightTextureColor * edgeMultiplier + topTextureColor * edgeMultiplier + bottomTextureColor * edgeMultiplier);

    float mag = length(color);

    float tmp = step(mag, 0.2);

    if (mag == 0.0){
        gl_FragColor = vec4(textureColor, 1.0);
        return;
    }

    gl_FragColor = (1.0 - tmp) * vec4(textureColor, 1.0) + tmp * vec4(0.0, 0.0, 1.0, 1.0);

    //    if (mag == 0.0){
    //        gl_FragColor = vec4(textureColor, 1.0);
    //    } else if (mag > 0.2){
    //        gl_FragColor = vec4(textureColor, 1.0);
    //    } else {
    //        gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);
    //    }

    //    gl_FragColor = vec4(color, texture2D(inputImageTexture, bottomTextureCoordinate).a);
}