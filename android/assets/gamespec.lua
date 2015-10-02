scene("first_scene") {
    background(R.image.background.nyaa),
}

local alice = object(R.character.alice) {
    x = 1.5,
    y = 0.5,
    z = 1,
    stance = "sleepy",
    clothes = "home",
    face = "happy",
}

frame("first_frame") {
    sequential() {
        alice:x{value = 0.7, interpolation = "elasticIn", time = 0.3},
        camera:bounds({0.15,0.15,0.85,0.85}),
        sound(R.sound.boom),
    },
    delay(1) {
        music(R.music.bgm1),
        character(R.character.alice),
        voice(R.voice.alice.chapter1.scene1.hello),
        text(R.text.chapter1.scene1.muu),
    }
}

local bob = object(R.character.bob) {
    x = -0.5,
    y = 0.5,
    z = 2,
    stance = "upright",
    clothes = "home",
    face = "happy",
}

frame("second_frame") {
    bob:x(0.3),
    character(R.character.bob),
    text(R.text.chapter1.scene1.hello),
}

local cg = object(R.image.oxygen) {
    bounds = {0,0,1,1},
    alpha = 0,
}

frame("third_frame") {
    cg:alpha(1),
    character(R.character.alice),
    text(R.text.yeay_oxygen),
    interactive("next_location1") {
        type = "multichoice",
        prompt = R.text.chapter1.whereToGoNext,
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
    }
}

frame("fifth_frame") {
    cg:alpha(0),
    conditional("next_location1", "school") {
        bob:x(1.5),
        delay(1) {
            alice:x(1.5),
        },
        goto(R.scene.chapter1.second_scene.first_frame),
    },
    conditional("next_location1", "home") {
        bob:x(-0.5),
        delay(1) {
            alice:x(-0.5),
        },
        goto(R.scene.chapter1.second_scene.second_frame),
    },
}

video(R.video.op)
