local x = 0
local y = 0
local objwidth = 0
local objheight = 0
local speedx = 0
local speedy = 0
local width = 0
local height = 0

function init(objw, objh)
    local width, height = engine:getDimensions()
    x = width / 2
    y = height / 2
    objwidth = objw
    objheight = objh
    local dir = math.random() * math.pi * 2
    speedx = math.cos(dir) * (width * height / 100000)
    speedy = math.sin(dir) * (width * height / 100000)
    print("Hello, world!")
end

function onDimensionsChanged(w, h)
    width = w
    height = h
end

function move()
    local a = nil
    local b = nil
    local c = 1
    local d = 2
    local e = 3
    local s = nil
    a, b, c, d, e, s = engine:test(a, b, c, d, e, s)
    b = {k=3}
    a, b, c, d, e, s = engine:test(a, b, c, d, e, s)
    a = 1
    a, b, c, d, e, s = engine:test(a, b, c, d, e, s)
    a = "blah"
    a, b, c, d, e, s = engine:test(a, b, c, d, e, s)


    x = x + speedx
    y = y + speedy
    if x <= 0 then
        x = 0
        speedx = -speedx
        engine:playSound("sfx")
    elseif x + objwidth >= width then
        x = width - objwidth
        speedx = -speedx
        engine:playSound("sfx")
    end
    if y <= 0 then
        y = 0
        speedy = -speedy
        engine:playSound("sfx")
    elseif y + objheight >= height then
        y = height - objheight
        speedy = -speedy
        engine:playSound("sfx")
    end
    return x, y
end

function onClick(x, y)
    print("Click at " .. x .. "," .. y)
end

print("File loaded!")
return 0
