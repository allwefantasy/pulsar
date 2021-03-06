package fun.platonic.pulsar.boilerpipe.filters.heuristics;

import fun.platonic.pulsar.boilerpipe.document.BlockLabels;
import fun.platonic.pulsar.boilerpipe.document.TextBlock;
import fun.platonic.pulsar.boilerpipe.document.TextDocument;
import fun.platonic.pulsar.boilerpipe.filters.TextBlockFilter;
import fun.platonic.pulsar.boilerpipe.utils.ProcessingException;

/**
 * Marks all blocks as content that:
 * <ol>
 * <li>are on the same tag-level as very likely main content (usually the level of the largest
 * block)</li>
 * <li>have a significant number of words, currently: at least 100</li>
 * </ol>
 */
public final class LargeBlockSameTagLevelToContentFilter implements TextBlockFilter {
  public static final LargeBlockSameTagLevelToContentFilter INSTANCE =
      new LargeBlockSameTagLevelToContentFilter();

  private LargeBlockSameTagLevelToContentFilter() {
  }

  public boolean process(final TextDocument doc) throws ProcessingException {

    boolean changes = false;

    int tagLevel = -1;
    for (TextBlock tb : doc.getTextBlocks()) {
      if (tb.isContent() && tb.hasLabel(BlockLabels.VERY_LIKELY_CONTENT)) {
        tagLevel = tb.getTagLevel();
        break;
      }
    }

    if (tagLevel == -1) {
      return false;
    }

    for (TextBlock tb : doc.getTextBlocks()) {
      if (!tb.isContent()) {

        if (tb.getNumWords() >= 100 && tb.getTagLevel() == tagLevel) {
          tb.setIsContent(true);
          changes = true;
        }
      }
    }

    return changes;

  }
}
