module testbench();
    wire [2:0] b;

    test t(b);
    test2 t2(b);

endmodule


module test(a);
    output reg [2:0] a;
    initial begin
        a <= 1;
    end
endmodule

module test2(a);
    input [2:0] a;
    initial begin
        #5 $display("a = %b", a);
    end
endmodule
