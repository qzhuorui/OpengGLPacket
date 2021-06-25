precision mediump float;
varying vec2 f_texturePos;
varying vec2 f_textureSize;
uniform sampler2D textureId;

int getIsStrokeWithAngel(float angle){
    float outlineSize = 2.0;

    int stroke = 0;
    float rad = angle * 0.01745329252;// 这个浮点数是 pi / 180，角度转弧度
    // 这句比较难懂，outlineSize * cos(rad)可以理解为在x轴上投影，除以textureSize.x是因为texture2D接收的是一个0~1的纹理坐标，而不是像素坐标
    float a = texture2D(textureId, vec2(f_texturePos.x + outlineSize * cos(rad) / f_textureSize.y, f_texturePos.y + outlineSize * sin(rad) / f_textureSize.x)).a;
    if (a >= 0.5){
        stroke = 1;
    }
    return stroke;
}

void main() {
    vec4 myC = texture2D(textureId, vec2(f_texturePos.x, f_texturePos.y));
    if (myC.a >= 0.5){
        gl_FragColor = myC;
        return;
    }

    int strokeCount = 0;
    strokeCount += getIsStrokeWithAngel(0.0);
    strokeCount += getIsStrokeWithAngel(30.0);
    strokeCount += getIsStrokeWithAngel(60.0);
    strokeCount += getIsStrokeWithAngel(90.0);
    strokeCount += getIsStrokeWithAngel(120.0);
    strokeCount += getIsStrokeWithAngel(150.0);
    strokeCount += getIsStrokeWithAngel(180.0);
    strokeCount += getIsStrokeWithAngel(210.0);
    strokeCount += getIsStrokeWithAngel(240.0);
    strokeCount += getIsStrokeWithAngel(270.0);
    strokeCount += getIsStrokeWithAngel(300.0);
    strokeCount += getIsStrokeWithAngel(330.0);
    strokeCount += getIsStrokeWithAngel(360.0);

    if (strokeCount >0){
        myC.rgb = vec3(0.0, 0.0, 1.0);
        myC.a =1.0;
    }
    gl_FragColor=myC;
}
