--cgs
state.cg=create(R.image.character.Yuri01,{
	cutout={0,320,1055,380}, --cutout(int startX, int startY, width, height), i wish to have this function to crop part of a picture as a CG
	x=0.5, 	-- (x, y) are the horizontal and verticle coordinates of geometric center of the pictrue
	y=0.3,	-- I don't know if it is possible to implement both ratio(x,y) and coordinate(x,y)
	interpolation="fade in",--maybe some other combinations of movements
	alpha=0,--to change alpha to show or hide cgs
	}) --I wonder if we can post multiple CGs
	--also a dispose function is needed to delete objects
state.cg:alpha(1)--change cg's alpha
dispose(state.cg)	--need to dispose cg and character objects

--characters
state.iwazawa=create(R.character.iwazawa,{
	--how do I know which picture is for which character? the R/image/character file is not include in the R/character file
	x=0.5,
	y=0.5,
	z=1,--higher means upper
	alpha=0,
	})--where to give coordinates for face layouts, or clothes
state.hinata:face("hinataSerious")--how to add the face layout on the layout of character. where do I specify the parameters?
dispose(state.hinata)--a function to dispose characters
--also a function to change alpha

--camera
camera(function ()
	camera:lookAt {400,390,1200,530}--look at{400,390,1200,530}of the background
	camera:interpolation(translate, {0,-310})--vector of camera's move:400,390,1200,530}->{400,80,1200,530}
	canmera:bounds {0,0,1,1}
	camera:time (3)--lasting time of the move
end) --how do we designate coordinates to initialize character if camera is lookAt certain textureRegion

--shader
shader()--maybe we can use shader functions inside libgdx to render shaders

--background
background(R.image.ep01.bg02,{effect="fadein"})--background may also need to fade in and out

--music
music(R.audio.music.CrowSong, true)--need to set loop or not and great if we have fade in and out effects
music(NIL)--need a function to stop the music

--text
text(R.text.ep01.otonashi.otonashi01)--text to display
--voice
voice(R.audio.voice.ep01.yuri01)--voice to display