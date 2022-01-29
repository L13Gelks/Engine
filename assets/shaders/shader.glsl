#type vertex
#version 330 core
layout(location=0) in vec3 aPos;
layout(location=1) in vec4 aColor;
layout(location=2) in vec2 aTextureCoordinates;
layout(location=3) in float aTextureID;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTextureCoordinates;
out float fTextureID;
out vec3 spritePos;

void main(){
    fColor = aColor;
    fTextureCoordinates = aTextureCoordinates;
    fTextureID = aTextureID;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
    spritePos = aPos;
}

    #type fragment
    #version 330 core

in vec4 fColor;
in vec2 fTextureCoordinates;
in float fTextureID;
in vec3 spritePos;

uniform sampler2D uTextures[8];
uniform int uLightsCount;
uniform float uLights[400];
uniform float uAmbientLight;


out vec4 color;

void main(){
    if(fTextureID > 0) {
        int id = int(fTextureID);
        vec4 textureColor = texture(uTextures[id], fTextureCoordinates);

        if(uLightsCount > 0){
            vec3 lightPosition;
            float light = 0.0f;
            float brightness = 0.0f;
            float finalLight = 0.0f;
            vec4 lightsColor = vec4(0,0,0,0);

            int counter = 0;
            for(int i = 0; i < uLightsCount; i += 8) {
//                lightPosition = vec3(uLights[i], uLights[i+1], 0);
//                light = uLights[i+2]/distance(lightPosition, spritePos);
//                brightness = clamp(dot(lightPosition, spritePos), 0.0, 1.0);
//                brightness *= clamp(1.0 - (length(lightPosition) / uLights[i+3]), 0.0, 1.0);
//                finalLight += light * brightness;
//                lightsColor += vec4((uLights[i+4] / 256)/distance(lightPosition, spritePos), (uLights[i+5] / 256)/distance(lightPosition, spritePos),
//                (uLights[i+6] / 256)/distance(lightPosition, spritePos), (uLights[i+7] / 256)/distance(lightPosition, spritePos));
//                counter++;
                lightPosition = vec3(uLights[i], uLights[i+1], 0);
                light = uLights[i+2]/distance(lightPosition, spritePos);
                brightness += clamp(dot(normalize(lightPosition), spritePos), 0.0, 1.0);
                brightness *= clamp(uLights[i+3] / 10, 0.0, 1.0);
                finalLight += light * brightness;

                lightsColor += vec4((uLights[i+4] / 256)/distance(lightPosition, spritePos), (uLights[i+5] / 256)/distance(lightPosition, spritePos),
                (uLights[i+6] / 256)/distance(lightPosition, spritePos), (uLights[i+7] / 256)/distance(lightPosition, spritePos));

                counter++;
            }
            color = (vec4(fColor.xyz * (max(uAmbientLight, finalLight)), fColor.a) * textureColor *
            max(vec4(uAmbientLight,uAmbientLight,uAmbientLight,uAmbientLight), lightsColor));
        }else{
            color = fColor * uAmbientLight * textureColor;
        }

        //color = fColor * textureColor;
    }else{
        color = fColor;
    }
}

