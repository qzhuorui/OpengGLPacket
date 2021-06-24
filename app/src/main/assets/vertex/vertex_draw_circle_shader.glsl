attribute vec4 av_Position;
attribute vec4 af_Color;
uniform mat4 u_Matrix;

varying vec4 v_texturePos;
varying vec4 f_textureColor;

void main() {
    //    v_texturePos = u_Matrix * av_Position;
    //    gl_Position = v_texturePos;
    //    f_textureColor = af_Color;

    //***************

    v_texturePos = u_Matrix * av_Position;

    vec4 center = vec4(0.0, 0.0, 0.0, 0.0);

    float distance = sqrt(pow(v_texturePos.x, 2.0) + pow(v_texturePos.y, 2.0));

    if (distance <= 0.5){
        f_textureColor = vec4(1.0, 1.0, 1.0, 1.0);
    } else {
        f_textureColor = af_Color;
    }
    gl_Position = v_texturePos;

}
