local linux = {}

function linux:onCreate(actor)
    self.actor = actor
    actor:setTexture(R.image.linux)
end

return linux
