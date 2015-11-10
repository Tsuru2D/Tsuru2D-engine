local alice = {}

function alice:onCreate(actor)
    self.actor = actor
    actor:setTexture(R.image.character.alice)
end

return alice
