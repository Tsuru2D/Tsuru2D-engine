local alice = {}

function alice:onCreate(actor)
    self.actor = actor
    actor:setTexture(R.image.alice)
end

return alice
