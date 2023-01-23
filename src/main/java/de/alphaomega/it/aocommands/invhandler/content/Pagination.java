package de.alphaomega.it.aocommands.invhandler.content;

import de.alphaomega.it.aocommands.invhandler.AOCItem;

import java.util.Arrays;

public interface Pagination {

    AOCItem[] getPageItems();

    int getPage();

    Pagination page(final int page);

    boolean isFirst();

    boolean isLast();

    Pagination first();

    Pagination previous();

    Pagination next();

    Pagination last();

    Pagination addToIterator(final SlotIterator iterator);

    Pagination setItems(final AOCItem... items);

    Pagination setItemsPerPage(final int itemsPerPage);

    class Impl implements Pagination {

        private int currentPage;

        private AOCItem[] items = new AOCItem[0];
        private int itemsPerPage = 5;

        @Override
        public AOCItem[] getPageItems() {
            return Arrays.copyOfRange(items, currentPage * itemsPerPage, (currentPage + 1) * itemsPerPage);
        }

        @Override
        public int getPage() {
            return this.currentPage;
        }

        @Override
        public Pagination page(final int page) {
            this.currentPage = page;
            return this;
        }

        @Override
        public boolean isFirst() {
            return this.currentPage == 0;
        }

        @Override
        public boolean isLast() {
            int pageCount = (int) Math.ceil((double) this.items.length / this.itemsPerPage);
            return this.currentPage >= pageCount - 1;
        }

        @Override
        public Pagination first() {
            this.currentPage = 0;
            return this;
        }

        @Override
        public Pagination previous() {
            if (!isFirst()) this.currentPage--;

            return this;
        }

        @Override
        public Pagination next() {
            if (!isLast()) this.currentPage++;

            return this;
        }

        @Override
        public Pagination last() {
            this.currentPage = this.items.length / this.itemsPerPage;
            return this;
        }

        @Override
        public Pagination addToIterator(final SlotIterator iterator) {
            for (AOCItem iS : getPageItems()) {
                iterator.next().set(iS);

                if (iterator.ended()) break;
            }

            return this;
        }

        @Override
        public Pagination setItems(final AOCItem... items) {
            this.items = items;
            return this;
        }

        @Override
        public Pagination setItemsPerPage(final int itemsPerPage) {
            this.itemsPerPage = itemsPerPage;
            return this;
        }

    }

}
