# 验证算法

# 需求

总体目标：验证两个面包板电路连接的等效性，给出错误和警告

具体需求：

1. 从Map对象或JSON字符串读取电路信息
3. 目标电路正确性验证：
   - 检查不完全在面包板上的元件（警告）
   - 检查不可能的连接，如面包板的一个孔中插入多个引脚（错误）
   - 分析连接性，消除位置信息，生成只包含元件连接关系的数据结构（错误）
   - 检查短路元件，包括导线（警告）
4. 目标电路与样本电路一致性验证：
   - 相同的数量和类型（错误）
   - 对应元件参数相同（警告）
   - 连接等效（错误）

# 概念说明

## 电路

电路是由元件组成的集合，组成电路的元件至少包含一个`Breadboard`。

## 元件

电子元器件的抽象表示，元件在电路中的位置记录在元件类内部，同时维护所有引脚的位置。

## Pass

验证过程中的一个步骤，整个验证过程被分解为若干个顺序执行的Pass，可以通过增减Pass和修改Pass来调整验证的功能。

# 使用

## 添加新的Pass

1. 在pass包下建立新的pass类，继承`Pass`类，pass类的类名必须以`Pass`结尾，例如`ImpossibleConnectionPass`

2. 实现构造函数，构造函数中需要调用父类构造函数，并指定pass的ID，建议ID与类名相同

   如果你的pass要求在必须在某些特定pass之后执行，请在构造函数中初始化`preRequirements`，将这些必须在你的pass之前执行的pass的ID添加到这个List中

3. 重载`execute`方法，这个方法是pass实际功能的入口，返回布尔值，代表pass是否执行成功。

   `execute`方法的第三个参数是已经执行的pass列表，可以利用这个参数配合父类的`checkPreRequirements`方法检查前置要求是否满足，如果不满足，可以直接返回`false`

以下是一个pass的示例：

```java
/**
 * Sample pass.
 * 
 * @author Lyc
 * @version 2023.03.18
 */
public class MyPass extends Pass {
    public MyPass() {
        // Call parent constructor
        super();
        // Set pass ID
        this.id = "MyPass";
        // Add pre-requirements
        this.preRequirements.add("UselessElementsPass");
    }

    @Override
    public Boolean execute(Circuit example, Circuit target, ArrayList<String> donePasses) throws Exception {
        // Check pre-requirements
        if(!this.checkPreRequirements(donePasses)) {
            this.addOutput(new Info("Pre-requirements not satisfied.", InfoType.ERROR));
            return false;
        }

        // Pass function here...
        
        return true;
    }
}
```

## 添加新的元件

1. 在`element`包下新建你的元件类，继承`Element`类

2. 实现构造函数，元件构造函数至少提供以下参数：

   | 参数                            | 说明                    |
   | ------------------------------- | ----------------------- |
   | `String id`                     | 验证器分配的ID          |
   | `String originId`               | 元件在原始电路中的ID    |
   | `int originX`                   | 元件在原始电路中的x坐标 |
   | `int originY`                   | 元件在原始电路中的y坐标 |
   | `ArrayList<Parameter> features` | 元件属性列表            |
   | `ArrayList<Pin> pins`           | 元件引脚列表            |

   这些参数就是`Element`类构造函数的参数，在子类的构造函数中调用父类构造函数，然后执行`analyseFeatures`方法

3. 重载`analyseFeatures`方法

   `Parameter`类代表元件的属性，为了方便，`Parameter`中属性的值都用字符串来表示，实际上，许多参数不是字符串类型，并且我们一般在元件的派生类中添加成员属性储存这些元件属性的值，`analyseFeatures`方法就是用来解析字符串并给这些成员属性赋值的

4. 重载`getType`方法

   返回代表这个元件种类的字符串

5. 重载`compareConnection`方法

   比较两个此类元件实例的连接是否相同，只要对应引脚连接的元件类型都相同就认为这两个元件连接相同。这样看来，似乎`compareConnection`在不同子类中的实现是相同的，但是，对于一些元件来说，引脚是没有顺序的，例如电阻，假如有两个电阻，它们首尾连接的元件类型刚好相反，但它们的连接依然应该被认为是相同的

   所以，`compareConnection`方法必须由子类提供不同的实现以正确的比较连接的相似性

以下是一个元件的示例：

```java
/**
 * Resistor
 * 
 * @author Lyc
 * @version 2023.03.18
 */
public class Resistor extends Element {
    private double resistance;
    
    public Resistor(String id,
                    String originId,
                    int originX,
                    int originY,
                    ArrayList<Parameter> features,
                    ArrayList<Pin> pins) {
        super(id, originId, originX, originY, features, pins);
        this.analyseFeatures();
    }

    @Override
    public String getType() {
        return "resistor";
    }

    @Override
    public Boolean compareConnection(Element e) {
        // Do comparing here...
        return true;
    }

    @Override
    protected void analyseFeatures() {
		// Assign resistance...
    }
}
```

