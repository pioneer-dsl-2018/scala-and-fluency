package pioneer.pictures

import pioneer.resource

object PictureProgram extends App {
  // flip a picture horizontally, grayscale it, and rotate it left
  Picture.flipHorizontal(resource("/image.png"), "output0.png")
  Picture.grayScale("output0.png", "output1.png")
  Picture.rotateLeft("output1.png", "output2.png")

  // The final picture is now in the top folder of this project's directory,
  // in a file called "output2.png".
}
