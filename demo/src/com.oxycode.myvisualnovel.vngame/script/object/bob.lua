local bob = {}

function bob:onCreate(actor)
    self.actor = actor
    actor:setTexture(R.image.bob)
end

return bob
