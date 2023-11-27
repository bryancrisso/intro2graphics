#version 140

in vec3 wc_frag_normal;        	// fragment normal in world coordinates (wc_)
in vec2 frag_texcoord;			// texture UV coordinates
in vec3 wc_frag_pos;			// fragment position in world coordinates

out vec3 color;			        // pixel colour

uniform sampler2D tex;  		  // 2D texture sampler
uniform samplerCube skybox;		  // Cubemap texture used for reflections
uniform vec3 wc_camera_position;  // Position of the camera in world coordinates



// Combined tone mapping and display encoding
vec3 tonemap(vec3 linearRGB)
{
    float L_white = 0.7; // Controls the brightness of the image

    float inverseGamma = 1./2.2;
    return pow(linearRGB/L_white, vec3(inverseGamma)); // Display encoding - a gamma
}



void main()
{
    const vec3 light_pos = vec3(-1,3,-1);
    const vec3 light_col = vec3(0.941, 0.968, 1);
    const vec3 I_a = vec3(0.1, 0.1, 0.1);
    const float kd = 0.4;
    const float ks = 0.75;
    const vec3 c_diff = vec3(1, 0, 0);
    const vec3 c_spec = vec3(1, 1, 1);
    const float alpha = 32;
    const float intensity = 80;
    float dist = length(light_pos - wc_frag_pos);
    vec3 I = light_col * (intensity / (3.14159 * 4 * pow(dist, 2)));

	vec3 linear_color = vec3(0, 0, 0);

    vec3 N = normalize(wc_frag_normal);
	// TODO: Calculate colour using Phong illumination model
    vec3 L = normalize(light_pos - wc_frag_pos);
    vec3 V = normalize(wc_camera_position-wc_frag_pos);
    vec3 R = normalize(reflect(-L, N));

    vec3 CR = normalize(reflect(-V, N));

    vec3 tex_col = vec3(texture(tex, frag_texcoord));
    vec3 sky_col = vec3(texture(skybox, CR));

    vec3 ambient = tex_col * I_a;
    vec3 diffuse = I * tex_col * light_col * kd * max(0, dot(N, L));
    vec3 specular = I * c_spec * light_col * ks * pow(max(0, dot(V, R)), alpha);

	linear_color = 0.9*(ambient + diffuse + specular) + 0.1*sky_col;
	// TODO: Sample the texture and replace diffuse surface colour (C_diff) with texel value


	color = tonemap(linear_color);
}

