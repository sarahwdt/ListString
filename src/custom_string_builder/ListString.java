package custom_string_builder;

class ListString {
    private StringItem head = null;
    //Позволит нам не создавать каждый раз переменную, для хранения указателя
    private StringItem tempIterableBlock;

    public ListString() {

    }

    //длина строки+
    public int length() {
        //Утрамбовываем элементы
        //tamp();
        int result = 0;
        //Проходим по всем блокам и суммируем их размеры
        for (tempIterableBlock = head; tempIterableBlock != null; tempIterableBlock = tempIterableBlock.next)
            result += tempIterableBlock.size;
        return result;
    }

    // символ в строке в позиции index - ГОТОВО!НЕ ТРОЖЬ!
    public char charAt(int index) {
        if (head == null)
            throw new StringIndexOutOfBoundsException();
        //Проходим по всем блоками и находим блок, в котором находится элемент с номером index
        for (tempIterableBlock = head, index -= head.size;
             tempIterableBlock != null;
             tempIterableBlock = tempIterableBlock.next, index -= tempIterableBlock.size) {
            //Блок с нужным элементом найден, когда index меньше нуля
            if (index < 0)
                //index в данном случае играет роль номера элемента с конца блока
                return tempIterableBlock.symbols[tempIterableBlock.size + index];
        }
        throw new StringIndexOutOfBoundsException();
    }

    //заменить символ в позиции index на ch - ГОТОВО! НЕ ТРОЖЬ!
    public void setCharAt(int index, char ch) {
        if (head == null)
            throw new StringIndexOutOfBoundsException();
        //Проходим по всем блоками и находим блок, в котором находится элемент с номером index
        for (tempIterableBlock = head, index -= head.size; tempIterableBlock != null; tempIterableBlock = tempIterableBlock.next, index -= tempIterableBlock.size) {
            //Блок с нужным элементом найден, когда index меньше нуля
            if (index < 0) {
                //index в данном случае играет роль номера элемента с конца блока
                tempIterableBlock.symbols[tempIterableBlock.size + index] = ch;
                return;
            }
        }
        throw new StringIndexOutOfBoundsException();
    }

    //ХУИТА
    //взятие подстроки, от start до end, не включая end TODO
    public ListString substring(int start, int end) {
        if (head == null)
            throw new StringIndexOutOfBoundsException();
        end = end - start;
        //Проходим по всем блоками и находим блок, в котором находится элемент с номером index
        for (this.tempIterableBlock = head, start -= head.size; tempIterableBlock != null;
             this.tempIterableBlock = this.tempIterableBlock.next, start -= this.tempIterableBlock.size) {
            //Блок с нужным элементом найден, когда index меньше нуля
            if (start < 0) {
                ListString result = new ListString();
                //index в данном случае играет роль номера элемента с конца блока
                result.head = createBlockChain(tempIterableBlock, tempIterableBlock.size + start, end);
                return result;
            }
        }
        throw new StringIndexOutOfBoundsException();
    }

    //добавить в конец символ+
    public void append(char ch) {
        //Собираем новый блок с одним символом
        StringItem newElement = new StringItem();
        newElement.symbols[0] = ch;
        newElement.size = 1;
        //Добавляем в начало, если список пуст иначе в конец
        if (head == null)
            head = newElement;
        else
            moveToEndAndGetIt().next = newElement;
    }

    //добавить в конец строку ListString+
    public void append(ListString string) {
        //Создаем копию аргумента и прикрепляем блоки копии к началу нашего списка, если он пуст, иначе к концу
        if (head == null)
            head = string.copy().head;
        else
            moveToEndAndGetIt().next = string.copy().head;
    }

    //добавить в конец строку String+
    public void append(String string) {
        //Создаем цепочку блоков на основе строки и прикрепляем к голове списка, если он пуст, иначе к концу
        if (head == null)
            head = createBlockChain(string);
        else
            moveToEndAndGetIt().next = createBlockChain(string);
    }

    //вставить в строку в позицию index строку ListString+
    public void insert(int index, ListString string) {
        //Копируем элемент
        string = string.copy();
        //Тут мы знаем где будет находится элемент поэтому сразу добавим его в начало
        if (index == 0 && head != null) {
            string.moveToEndAndGetIt().next = head;
            head = string.head;
            return;
        }
        //Проходим по всем блоками и находим блок, в котором находится элемент с номером index
        for (tempIterableBlock = head, index -= head.size;
             tempIterableBlock != null;
             tempIterableBlock = tempIterableBlock.next, index -= tempIterableBlock.size) {
            //Блок с нужным элементом найден, когда index меньше нуля
            if (index < 0) {
                string.moveToEndAndGetIt().next = splitByIndexAndGetTail(tempIterableBlock, tempIterableBlock.size + index);
                moveToEndAndGetIt().next = string.head;
                return;
            }
        }
        throw new StringIndexOutOfBoundsException();
    }

    //вставить в строку в позицию index строку ListString+
    public void insert(int index, String string) {
        StringItem blockChain = createBlockChain(string);
        StringItem iterator = blockChain;
        while (iterator.next != null)
            iterator = iterator.next;
        //Тут мы знаем где будет находится элемент поэтому сразу добавим его в начало
        if (index == 0 && head != null) {
            iterator.next = head;
            head = blockChain;
            return;
        }
        //Проходим по всем блоками и находим блок, в котором находится элемент с номером index
        for (tempIterableBlock = head, index -= head.size;
             tempIterableBlock != null;
             tempIterableBlock = tempIterableBlock.next, index -= tempIterableBlock.size) {
            //Блок с нужным элементом найден, когда index меньше нуля
            if (index < 0) {
                iterator.next = splitByIndexAndGetTail(tempIterableBlock, tempIterableBlock.size + index);
                moveToEndAndGetIt().next = blockChain;
                return;
            }
        }
        throw new StringIndexOutOfBoundsException();
    }

    //переопределить метод+
    @Override
    public String toString() {
        //Создаем массив для элементов
        char[] string = new char[length()];
        //Проходим по всем значимым элементам и собираем их в массив
        tempIterableBlock = head;
        for (int i = 0; tempIterableBlock != null; tempIterableBlock = tempIterableBlock.next) {
            for (int j = 0; j < tempIterableBlock.size; j++, i++)
                string[i] = tempIterableBlock.symbols[j];
        }
        //Преобразуем к строке и возвращаем
        return String.valueOf(string);
    }

    public class StringItem {
        private final char[] symbols = new char[16];
        private StringItem next;
        private byte size;

        public StringItem() {
        }

        //Создание копии блока - ГОТОВО! НЕ ТРОЖЬ!
        private StringItem copy() {
            //Создаем пустой блок
            StringItem copy = new StringItem();
            //Копируем символы
            for (int i = 0; i < size; i++)
                copy.symbols[i] = this.symbols[i];
            //Копируем размер
            copy.size = this.size;
            //Не имеет смысл создавать копию на следующий блок, так как рекурсивные вызовы запрещены
            return copy;
        }

    }

    //Копия листа+
    public ListString copy() {
        //Создаем пустой лист
        ListString copy = new ListString();
        //Присваеваем начальному блоку копии копию начального блока текущего списка
        copy.head = this.head.copy();
        //По цепочке присваеваем копии следующих блоков слудующим блокам копии
        this.tempIterableBlock = this.head;
        for (this.tempIterableBlock = this.head, copy.tempIterableBlock = copy.head; this.tempIterableBlock.next != null;
             this.tempIterableBlock = this.tempIterableBlock.next, copy.tempIterableBlock = copy.tempIterableBlock.next)
            copy.tempIterableBlock.next = this.tempIterableBlock.next.copy();
        return copy;
    }

    //Передвигаем метку current в конец списка и возвращает конечный блок+
    private StringItem moveToEndAndGetIt() {
        tempIterableBlock = head;
        while (tempIterableBlock.next != null)
            tempIterableBlock = tempIterableBlock.next;
        return tempIterableBlock;
    }

    //Разделяет список на две части блоков начиная с индекса+
    private StringItem splitByIndexAndGetTail(StringItem block, int index) {
        //Создаем новый блок
        StringItem tail = new StringItem();
        //Начиная с индекса добавляем элементы в новый блок и удаляем из старого
        for (int i = index; i < block.size; i++) {
            tail.symbols[i - index] = block.symbols[i];
            block.symbols[i] = '\0';
        }
        //Новые размеры блоков
        tail.size = (byte) (block.size - index);
        block.size = (byte) index;
        //Новый блок прикрепляем к началу хвоста
        tail.next = block.next;
        //А у старого блока удаляем хвост
        block.next = null;
        return tail;
    }

    private StringItem createBlockChain(StringItem block, int start, int length) {
        StringItem blockChain = new StringItem();
        int index = 0;
        for (; block != null; block = block.next) {
            for (int i = start; i < block.size && length > 0; i++, index++, length--) {
                if (index == blockChain.symbols.length) {
                    blockChain.size = (byte) blockChain.symbols.length;
                    blockChain.next = new StringItem();
                    blockChain = blockChain.next;
                    index = 0;
                }
                blockChain.symbols[index] = block.symbols[i];
            }
            start = 0;
        }
        blockChain.size = (byte) index;
        if (length > 0)
            throw new ArrayIndexOutOfBoundsException();
        else
            return blockChain;
    }

    //Создает цепочку блоков на основе строки+
    private StringItem createBlockChain(String string) {
        //Создаем новый блок и итератор для блока
        StringItem chain = new StringItem();
        tempIterableBlock = chain;
        //Количество элементов строки пересчитываем один раз
        int stringLength = string.length();
        int index = 0;
        //Цикл по всем элементам строки
        for (int i = 0; i < stringLength; i++, index++) {
            //Условие перехода к следующему блоку цепочки
            if (index == tempIterableBlock.symbols.length) {
                //Устанавливаем размер, создаем следующий блок и передвигаем итератор на следующий блок
                tempIterableBlock.size = (byte) tempIterableBlock.symbols.length;
                tempIterableBlock.next = new StringItem();
                tempIterableBlock = tempIterableBlock.next;
                index = 0;
            }
            //Копируем символы из строки в блок
            tempIterableBlock.symbols[index] = string.charAt(i);
        }
        //Устанавливаем размер для последнего блока
        tempIterableBlock.size = (byte) index;
        return chain;
    }

    //Утрамбовывает элементы в списке+
    private void tamp() {
        if (head == null)
            return;
        //Указатель на цепочку блоков в которую будут утрамбовываться элементы
        tempIterableBlock = head;
        int index = 0;
        //Цикл по всем блокам списка
        for (StringItem currentBlock = head; currentBlock != null; currentBlock = currentBlock.next) {
            for (int j = 0; j < currentBlock.size; index++, j++) {
                //Условие переходу к следующему блоку в утрамбованной цепочке
                if (index == tempIterableBlock.symbols.length) {
                    //Устанавливает размер
                    tempIterableBlock.size = (byte) tempIterableBlock.symbols.length;
                    //Переходим к следующему элементу
                    tempIterableBlock = tempIterableBlock.next;
                    //Обнуляем индекс
                    index = 0;
                }
                //Копирует элемент из списка в утрамбованный список
                tempIterableBlock.symbols[index] = currentBlock.symbols[j];
            }
        }
        //Устанавливет размер
        tempIterableBlock.size = (byte) index;
        //Чистит все оставшиеся элементы
        for (; index < tempIterableBlock.symbols.length; index++)
            tempIterableBlock.symbols[index] = '\0';
        //Удаляем указатель на следующий элемент
        tempIterableBlock.next = null;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ListString test = new ListString();
        System.out.println("");
    }

}
