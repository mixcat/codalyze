# Introduction #

Arduino is used to drive an array of blinkms


# Details #

Blinkms are smart components attached to an I2C bus,
which has a limited maximum length (tops few meters, no more than one in my first installment).

The 100 blinkms will be spaced somewhere between 4 and 10 cm: a gross bus length
estimate is 4/10m, well beyond I2C max length.

To overcome this, I'm trying out some I2C bus extender chips, made by NXP, which should allow up to 50m.

P82B715PN - http://www.nxp.com/#/pip/pip=[pip=P82B715_7]|pp=[t=pip,i=P82B715_7]

Add your content here.  Format your content with:
  * Text in **bold** or _italic_
  * Headings, paragraphs, and lists
  * Automatic links to other wiki pages