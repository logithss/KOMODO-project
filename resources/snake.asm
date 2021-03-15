:S_start

:S_title
;1)clear screen
;2)draw title screen
;3)move snake to corner of title and set direction
;4)set size of fake snake
;5)set body nodes to head body position(reverse body move)
;6)set fake fruit position using a lookup table(index is snake direction)
;7)move body
;)move head
;)check if collision with fake fruit
;yes? -> increment direction; new fake fruit position
;check keyboard input; if enter key, GOTO s_game; if 'M' key, mute volume
;GOTO 7) (keep loop until player starts game)


:S_game
;1)clear screen
;2)reset snake (position, size, direction)
;3)GOTO S_readyRoutine
;:S_newFrame
;4)GOTO S_moveBody
;)GOTO S_movehead
;)check head against bounds ? yes: GOTO S_gameOver
;)check head against body ? yes: GOTO S_gameOver 
;)check head against fruit ? yes: GOTO S_score; GOTO newFruit;
;)GOTO S_drawGame
;)GOTO S_gameDelay
;)JMP S_newFrame


:S_gameover
:S_redSnakeRoutine
;)set all characters of snake to X
;)draw snake red
;)wait 250 ms
;)draw snake white
;)wait 250 ms
;) if counter is done, return; else, jump to S_redSnakeRoutine
;)wait 1000 ms
;draw gameOverScreen
;check keyboard input; if enter key, GOTO s_game
