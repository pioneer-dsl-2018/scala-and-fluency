package pioneer

import pioneer.pictures.Pictures

object Program extends App {
  // Flip a picture horizontally and save the result to disk
  Pictures.flipHorizontal(resource("/image.png"), "output.png")
}
