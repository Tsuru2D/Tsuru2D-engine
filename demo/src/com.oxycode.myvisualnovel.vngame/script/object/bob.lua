local bob = {}

function bob:onCreate(actor)
    self.actor = actor
    actor:setTexture(R.image.character.bob)
end

return bob
