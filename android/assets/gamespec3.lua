scene(R.scene.first_scene) {
    background = R.image.background.nyaa,
}

create(R.character.alice) {
    x = 1.5,
    y = 0.5,
    z = 1,
    stance = R.character.bob.stance.upright,
    clothes = R.character.bob.clothes.home,
    face = R.character.bob.face.happy,
}

frame("first_frame") {
    transform(R.character.alice) {
        face = R.character.alice.face.angry,
        x = {value = 0.7, interpolation = "elasticIn", time = 0.3},
    },
    sound(R.sound.boom),
    music(R.music.bgm1),
    text(R.text.hello),
    camera {
        bounds = {
            value = {0,0,1,1},
            interpolation = "elasticIn",
            time = 0.3
        },
    },
}

create(R.character.bob) {
    x = -0.5,
    y = 0.5,
    z = 2,
    stance = R.character.alice.stance.sleepy,
    clothes = R.character.alice.clothes.home,
    face = R.character.alice.face.happy,
}

frame("second_frame") {
    transform(R.character.bob) {x = 0.3},
    text(R.text.what)
}

frame("third_frame") {
    cg(R.image.oxygen) {
        bounds = {0,0,1,1}
    },
    text(R.text.yeay_oxygen)
}

frame("fourth_frame") {
    interactive {
        type = "multichoice",
        prompt = R.text.chapter1.whereToGoNext,
        key = "next_location1",
        choices = {
            {
                value = "next_location1_school",
                text = R.text.chapter1.gotoSchool,
                frame = "scene_school"
            },
            {
                value = "next_location1_home",
                text = R.text.chapter1.gotoHome,
                frame = "scene_home"
            }
        }
    }
}

frame("fifth_frame") {
    transform(R.character.bob) {x=1.5},
    transform(R.character.alice) {x=1.5}
}

destroy(R.character.bob)
destroy(R.character.alice)

video(R.video.op)

scene(R.scene.second_scene) {
    background = R.image.background.pomf
}

frame("some_frame") {

}
