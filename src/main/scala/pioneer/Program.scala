package pioneer

import pioneer.pictures.Pictures

object Program extends App {
  // flip a picture horizontally, grayscale it, and rotate it left
  Pictures.flipHorizontal(resource("/image.png"), "output0.png")
  Pictures.grayScale("output0.png", "output1.png")
  Pictures.rotateLeft("output1.png", "output2.png")

  // The final picture is now in the top folder of this project's directory,
  // in a file called "output2.png".
}
