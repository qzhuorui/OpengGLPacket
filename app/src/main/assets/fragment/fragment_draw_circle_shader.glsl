precision mediump float;
varying vec4 f_textureColor;

void main() {
    gl_FragColor = f_textureColor;

    //***************

    //    vec2 center = vec2(0.0, 0.0);
    //    vec2 distanceVec = gl_FragCoord.xy -center.xy;
    //    float fragToCenterLength = sqrt(pow(distanceVec.x, 2) + pow(distanceVec.y, 2));
    //    if (fragToCenterLength>0.5){
    //        gl_FragColor = vec4(f_textureColor.rgb, 0.0);
    //    } else {
    //        gl_FragColor = vec4(f_textureColor.rgb, 1.0);
    //    }
}
