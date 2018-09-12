#version 400 core

in vec2 passTextureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in vec3 passEffectColor;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;

void main(void){
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	vec3 unitCameraVector = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
	float specularFactor = dot(reflectedLightDirection,unitCameraVector);
	specularFactor = max(specularFactor,0.0);
	float dampedFactor = pow(specularFactor,shineDamper);
	vec3 specular = dampedFactor * lightColour * reflectivity;
	float nDotl = dot(unitNormal,unitLightVector);
	float brightness = max(nDotl,0.5);
	vec3 diffuse = brightness * lightColour;
	vec4 textureColor = texture(textureSampler,passTextureCoords);
	vec4 actualColor = textureColor;
	if(passEffectColor.x >= 0)
		actualColor = vec4((textureColor.x + passEffectColor.x * 4) / 5, (textureColor.y + passEffectColor.y * 4) / 5, (textureColor.z + passEffectColor.z * 4) / 5, 1.0);
	out_Color = vec4(diffuse,1.0) * actualColor + vec4(specular,1.0);
}