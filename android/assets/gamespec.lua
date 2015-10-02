scene("first_scene", {
    -- todo: what goes here? inter-scene transitions?
})

local alice = create(R.character.alice, {
    x = 1.5,
    y = 0.5,
    z = 1,
    stance = "sleepy",
    clothes = "home",
    face = "happy",
})

frame("first_frame", function()
    background(R.image.background.nyaa) -- todo: should bg be on scene or frame?
    sequential(function()
        alice:x(0.7, {interpolation = "elasticIn", time = 0.3}) -- todo: any better syntaxes for this?
        camera:bounds({0.15,0.15,0.85,0.85}) -- todo: camera effects (bounce, shake, etc)
        camera:shader(R.shader.blur) -- todo: shader transitions (uniform/attribute)
        sound(R.sound.boom)
    end)
    delay(1, function()
        music(R.music.bgm1)
        character(R.character.alice)
        voice(R.voice.alice.chapter1.scene1.hello)
        text(R.text.chapter1.scene1.muu)
    end)
end)

local bob = create(R.character.bob, {
    x = -0.5,
    y = 0.5,
    z = 2,
    stance = "upright",
    clothes = "home",
    face = "happy",
})

frame("second_frame", function()
    bob:x(0.3)
    camera:bounds({0,0,1,1})
    character(R.character.bob)
    text(R.text.chapter1.scene1.hello)
    voice("auto")
end)

local cg = create(R.image.oxygen, {
    bounds = {0,0,1,1},
    alpha = 0,
})

frame("third_frame", function()
    cg:alpha(1)
    character(R.character.alice)
    voice("auto")
    text(R.text.chapter1.scene1.where_should_we_go)
    interactive("next_location1", {
        type = "multichoice",
        prompt = R.text.chapter1.where_should_we_go,
        choices = {
            {
                value = "school",
                text = R.text.chapter1.gotoSchool,
            },
            {
                value = "home",
                text = R.text.chapter1.gotoHome,
            }
        }
    })
end)

frame("fourth_frame", function()
    cg:alpha(0)
    if var("next_location1") == "school" then
        bob:x(1.5)
        delay(1, function()
            alice:x(1.5)
        end)
        goto(R.scene.chapter1.second_scene.first_frame)
    else
        bob:x(-0.5)
        delay(1, function()
            alice:x(-0.5)
        end)
        goto(R.scene.chapter1.second_scene.second_frame)
    end
end)

-- todo: is a delete function necessary?
-- todo: or can we just rely on the gc?
delete(cg)
delete(bob)
delete(alice)

video(R.video.op)

splash(R.image.splash.generic)



-----------------------------------------
-- Here's a less contrived example
-----------------------------------------
scene("second_scene", {
    -- still no idea what goes in here...
})
local alice = create(R.character.alice, {
    x = 1.5,
    y = 0.5,
    z = 1,
    stance = "sleepy",
    clothes = "home",
    face = "happy",
})
frame("f1", function()
    alice:x(0.3)
    background(R.image.bg.school)
    character(R.character.alice)
    text(R.text.chapter1.scene2.hello_world)
end)
frame("f2", function()
    character(R.character.alice)
    text(R.text.chapter1.scene2.this_is_a_test)
end)
frame("f3", function()
    character(R.character.alice)
    text(R.text.chapter1.scene2.whats_your_name)
    interactive("name", {type = "textbox"})
end)
frame("f4", function()
    character(R.character.alice)
    text(R.text.chapter1.scene2.fmt1_hi_there, var("name")) -- string formatting
end)
local cg = create(R.image.cg.alice_happy, {
    bounds = {0,0,1,1},
    alpha = 0,
})
frame("f5", sequential(function()
    cg:alpha(1)
    character(R.character.alice)
    text(R.text.chapter1.scene2.this_is_a_cg)
end))
frame("f6", function()
    cg:alpha(0)
    text(R.text.chapter1.scene2.narrator_blah)
end)
delete(cg)
frame("f7", function()
    character(R.character.alice)
    text(R.text.chapter1.scene2.gotta_go_bye)
    delay(1, function()
        alice:x(1.5)
    end)
end)
delete(alice)
