return {
    table {
        button {
            text = R.text.common.settings,
            onClick = function(screen, button)
                screen:pushScreen({
                    screen = R.screen.options,
                    transition = "fade",
                    args = {}
                })
            end
        }
    }
}
