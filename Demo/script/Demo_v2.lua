scene ("scene01", function()
	setup(function(state)
		state.cg=create(R.image.character.Yuri01,{
			cutout={0,320,1055,380}, --cutout(int startX, int startY, width, height), i wish to have this function to crop part of a picture as a CG
			x=0.5, 	-- (x, y) are the horizontal and verticle coordinates of geometric center of the pictrue
			y=0.3,	-- I don't know if it is possible to implement both ratio(x,y) and coordinate(x,y)
			interpolation="fade in",
			alpha=0,
			}) --I wonder if we can post multiple CGs
			--also a dispose function is needed to delete objects
	end)
	frame("frame01",function ()
		background(R.image.ep01.bg01)
		delay(0.5, function(state)
			state.cg:alpha(1).next(function ()--change cg's alpha
				character(R.character.yuri)
				text(R.text.ep01.Yuri.yuri01)
				voice(R.audio.voice.ep01.yuri01)	
			end)
		end)
		dispose(state.cg)	--need to dispose cg and character objects
	end)
	frame("frame02",function ()
		background(R.image.ep01.bg02)
		text(R.text.ep01.otonashi.otonashi01)
	end)
	frame("frame03",function ()
		text(R.text.ep01.otonashi.otonashi02)
	end)
	frame("frame04",function ()
		character(R.character.otonashi)
		text(R.text.ep01.otonashi.otonashi03)
	end)
	frame("frame05",function ()
		text(R.text.ep01.otonashi.otonashi04)
	end)
	frame("frame06",function ()
		text(R.text.ep01.otonashi.otonashi05)
	end)
end)

scene("scene02",function()
	setup(function()
		
	end)
	frame("frame11",function ()
		background(R.image.ep01.bg03)
		text(R.text.ep01.otonashi.otonashi05)
		music(R.audio.music.CrowSong)
		camera(function ()
			camera:lookAt {400,390,1200,530}--look at{400,390,1200,530}of the background
			camera:interpolation(translate, {0,-310})--vector of camera's move:400,390,1200,530}->{400,80,1200,530}
			canmera:bounds {0,0,1,1}
			camera:time (3)--lasting time of the move
		end)--an other question is how do we designate coordinates to initialize character if camera is lookAt some textureRegion
	end)
	frame("frame12",function ()
		text(R.text.ep01.otonashi.otonashi06)
	end)
	frame("frame13",function ()
		text(R.text.ep01.otonashi.otonashi07)
	end)
	frame("frame14",function ()
		text(R.text.ep01.otonashi.otonashi08)
	end)
	frame("frame15",function ()
		text(R.text.ep01.otonashi.otonashi09)
	end)
	frame("frame16",function ()
		text(R.text.ep01.otonashi.otonashi10)
	end)
	frame("frame17",function ()
		text(R.text.ep01.otonashi.otonashi11)
	end)

end)

scene("scene03",function ()
	setup(function(state)
		state.iwazawa=create(R.character.iwazawa,{
			--how do we know which picture is for which character? the R/image/character file is not include in the R/character file
			x=0.5,
			y=0.5,
			z=1,
			alpha=0,
			})--where to give coordinates for face layouts
		state.hinata=create(R.character.hinata,{
			x=0.5,
			y=0.5,
			z=1,
			alpha=0,
			})
	end)

	frame("frame21",function (state)
		background(R.image.ep01.bg04)
		state.iwazawa:alpha(1)
		shader()--maybe we can use shader functions inside libgdx to render shaders
		character(R.character.yuri)
		text(R.text.ep01.yuri.yuri02)
		--voice to be added
	end)
	dispose(state.iwazawa)
	frame("frame22",function (state)
		background(R.image.ep01.bg05)
		state.hinata:alpha(1)
		shader()
		character(R.character.hinata)
		text(R.text.ep01.hinata.hinata01)
		--voice to be added
	end)
	dispose(state.hinata)
	frame("frame23", function ()
		character(R.character.otonashi)
		text(R.text.ep01.otonashi.otonashi12)
	end)
	frame("frame24", function (state)
		state.hinata:face("hinataSerious")--how to add the face layout on the layout of character. where do I specify the parameters?
		character(R.character.hinata)
		text(R.text.ep01.hinata.hinata02)
		--voice to be added
	end)
	frame("frame25",function()
		background(R.image.ep01.bg03)
		camera(function ()
			camera:lookAt {400,80,1200,530}
			end)
		text(R.text.ep01.otonashi.otonashi13)
	end)
	frame("frame26",function ()
		text(R.text.ep01.otonashi.otonashi14)
	end)
	frame("frame27",function ()
		text(R.text.ep01.otonashi.otonashi15)
	end)
end)

scene("scene04",function ()
	frame("frame31",function ()
		background(R.image.ep01.bg02,{effect="fadein"})--background may also need to fade in and out
		text(R.text.ep01.otonashi.otonashi16)
	end)
end)
