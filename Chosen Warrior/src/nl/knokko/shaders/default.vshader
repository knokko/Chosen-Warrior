#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 passTextureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
out vec3 passEffectColor;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform vec3 effectColor;

void main(void){
	vec4 worldPosition;
	worldPosition = transformationMatrix * vec4(position,1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	passTextureCoords = textureCoords;
	passEffectColor = effectColor;
	surfaceNormal = (transformationMatrix * vec4(normal,0.0)).xyz;
	toLightVector = lightPosition - worldPosition.xyz;
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;
}