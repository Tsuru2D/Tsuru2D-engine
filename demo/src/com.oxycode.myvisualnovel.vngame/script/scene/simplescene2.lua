return function(scene)
    scene:setup(function(frame, state)
        state.alice = frame:create(R.object.alice, {
            x = 1500,
            y = 80
        })
        state.bob = frame:create(R.object.bob, {
            x = -400,
            y = 80
        })
        frame:background(R.image.background.bg2)
    end)
    scene:frame("f1", function(frame, state)
        frame:transform(state.bob, {
            x = {value = 200, duration = 1, interpolation = "pow2"}
        })
        frame:transform(state.alice, {
            x = {value = 800, duration = 1, interpolation = "pow2"}
        })
    end)
    scene:frame("f2", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene2.hey_alice)
    end)
    scene:frame("f3", function(frame, state)
        frame:character(R.object.alice)
        frame:text(R.text.simplescene2.hey_bob)
    end)
    scene:frame("f4", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene2.giving_demo)
    end)
    scene:frame("f4", function(frame, state)
        frame:character(R.object.alice)
        frame:text(R.text.simplescene2.whats_that)
    end)
    scene:frame("f5", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene2.cross_platform_engine)
    end)
    scene:frame("f6", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene2.runs_on)
    end)
    scene:frame("f7", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene2.sync_data)
    end)
    scene:frame("f8", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene2.written_in)
    end)
    scene:frame("f9", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene2.wait_what)
    end)
    scene:frame("f10", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene2.lets_fix_that)
    end)
    scene:frame("f11", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene2.see_you_all)
    end)
    scene:frame("f12", function(frame, state)
        frame:transform(state.bob, {
            x = {value = 1500, duration = 1, interpolation = "pow2"}
        })
        frame:transform(state.alice, {
            x = {value = -400, duration = 1, interpolation = "pow2"}
        })
    end)
    return R.scene.simplescene1
end
